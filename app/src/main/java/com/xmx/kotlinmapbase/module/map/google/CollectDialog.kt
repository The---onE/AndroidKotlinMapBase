package com.xmx.kotlinmapbase.module.map.google

import android.app.AlertDialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.data.dataManager
import com.xmx.kotlinmapbase.common.map.gmap.collection.Collection
import com.xmx.kotlinmapbase.common.map.gmap.collection.ICollectionManager
import com.xmx.kotlinmapbase.common.map.gmap.collection.collectionManager
import com.xmx.kotlinmapbase.utils.StringUtil
import kotlinx.android.synthetic.main.dialog_collect.*

/**
 * Created by The_onE on 2017/2/28.
 * 收藏POI对话框
 * @property[mContext] 当前上下文
 * @property[position] 收藏点的位置
 * @property[title] 标题框默认显示的标题
 * @property[onSuccess] 收藏成功后的操作
 */
class CollectDialog(val mContext: Context, val position: LatLng, var title: String? = null,
                    val onSuccess: ((Collection) -> Unit))
    : DialogFragment() {
    var type: String? = null
    // 是否为修改对话框
    var mModifyFlag = false
    // 要修改的收藏
    var mCollection: Collection? = null

    // 收藏管理器
    var cManager: ICollectionManager<Collection> = collectionManager

    /**
     * 修改收藏对话框
     * @param[context] 当前上下文
     * @param[collection] 要修改的收藏
     * @param[onSuccess] 修改成功的操作
     */
    constructor(context: Context, collection: Collection, onSuccess: ((Collection) -> Unit))
            : this(context, collection.mPosition, collection.mTitle, onSuccess) {
        mModifyFlag = true
        mCollection = collection
        type = collection.mType
        cManager.changeTable(dataManager.collectionTableName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_collect, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 不显示默认标题栏
        dialog.requestWindowFeature(STYLE_NO_TITLE)
        // 填充标题框
        title?.apply { editTitle.setText(this) }
        // 填充描述框
        mCollection?.apply { editContent.setText(mContent) }
        // 设置类型相关事件
        if (mapConstantsManager.getTypeList().isNotEmpty()) {
            val list = mapConstantsManager.getTypeList()
            if (type.isNullOrEmpty()) {
                // 默认显示第一种类型
                type = mapConstantsManager.getTypeList()[0]
            }
            imgType.setImageResource(mapConstantsManager.getIconId(type!!)!!)
            // 点击类型图标
            imgType.setOnClickListener {
                // 弹出选择类型对话框
                AlertDialog.Builder(mContext).setTitle("类型")
                        // 将可选类型列出
                        .setItems(list.toTypedArray(), {
                            dialogInterface, i ->
                            // 获取选择的类型
                            type = list[i]
                            // 更改选择的图标
                            val iconId = mapConstantsManager.getIconId(type!!)
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
            if (type == null) {
                // 若未设置类型
                if (mapConstantsManager.getTypeList().isNotEmpty()) {
                    // 设置为默认类型
                    type = mapConstantsManager.getTypeList()[0]
                } else {
                    return@setOnClickListener
                }
            }
            if (!mModifyFlag) {
                // 添加新收藏
                // 生成收藏
                val col = Collection(position, editTitle.text.toString(),
                        type!!, editContent.text.toString())
                // 添加收藏
                cManager.insertToCloud(col,
                        success = {
                            user, id ->
                            StringUtil.showToast(mContext, "收藏成功")
                            col.cloudId = id
                            onSuccess(col)
                            dismiss()
                        },
                        error = cManager.defaultError(mContext),
                        cloudError = cManager.defaultCloudError(mContext)
                )
            } else {
                // 修改收藏
                mCollection?.apply {
                    mTitle = editTitle.text.toString()
                    this.mType = type!!
                    mContent = editContent.text.toString()
                    // 插入带有Cloud Id的实体会覆盖之前的实体
                    cManager.insertToCloud(this,
                            success = {
                                user, id ->
                                StringUtil.showToast(mContext, "修改成功")
                                onSuccess(this)
                                dismiss()
                            },
                            error = cManager.defaultError(mContext),
                            cloudError = cManager.defaultCloudError(mContext))
                }
            }
        }
        // 取消
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}
