package com.xmx.kotlinmapbase.module.map.google

import android.app.AlertDialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.map.gmap.collection.Collection
import com.xmx.kotlinmapbase.common.map.gmap.collection.collectionManager
import com.xmx.kotlinmapbase.utils.StringUtil
import kotlinx.android.synthetic.main.dialog_collect.*


/**
 * Created by The_onE on 2017/2/28.
 * 收藏POI对话框
 * @property[position] 收藏点的位置
 * @property[title] 标题框默认显示的标题
 */
class CollectDialog(val mContext: Context, val position: LatLng, var title: String? = null)
    : DialogFragment() {
    var mType: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_collect, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 不显示默认标题栏
        dialog.requestWindowFeature(STYLE_NO_TITLE)
        // 填充标题框
        title?.apply { editTitle.setText(this) }

        // 设置类型相关事件
        if (collectionTypeManager.getTypeList().isNotEmpty()) {
            val list = collectionTypeManager.getTypeList()
            // 默认显示第一种类型
            mType = collectionTypeManager.getTypeList()[0]
            imgType.setImageResource(collectionTypeManager.getIconId(mType!!)!!)
            // 点击类型图标
            imgType.setOnClickListener {
                // 弹出选择类型对话框
                AlertDialog.Builder(mContext).setTitle("类型")
                        // 将可选类型列出
                        .setItems(list.toTypedArray(), {
                            dialogInterface, i ->
                            // 获取选择的类型
                            mType = list[i]
                            // 更改选择的图标
                            val iconId = collectionTypeManager.getIconId(mType!!)
                            imgType.setImageResource(iconId!!)
                        })
                        .setNegativeButton("取消", {
                            dialog, id ->
                            dismiss()
                        })
                        .show()
            }
        }

        // 确认
        btnConfirm.setOnClickListener {
            if (mType == null) {
                // 若未设置类型
                if (collectionTypeManager.getTypeList().isNotEmpty()) {
                    // 设置为默认类型
                    mType = collectionTypeManager.getTypeList()[0]
                } else {
                    return@setOnClickListener
                }
            }
            // 生成收藏
            val col = Collection(position, editTitle.text.toString(),
                    mType!!, editContent.text.toString())
            // 添加收藏
            collectionManager.insertToCloud(col,
                    success = {
                        id ->
                        StringUtil.showToast(mContext, "收藏成功")
                        dismiss()
                    },
                    error = collectionManager.defaultError(mContext),
                    cloudError = collectionManager.defaultCloudError(mContext)
            )
        }
        // 取消
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}
