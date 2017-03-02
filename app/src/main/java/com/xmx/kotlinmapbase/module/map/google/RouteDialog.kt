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
import com.xmx.kotlinmapbase.common.map.gmap.route.routeManager
import com.xmx.kotlinmapbase.common.map.gmap.route.Route
import com.xmx.kotlinmapbase.utils.StringUtil
import kotlinx.android.synthetic.main.dialog_route.*

/**
 * Created by The_onE on 2017/3/2.
 * 添加路线对话框
 */
class RouteDialog(val mContext: Context,
                  val start: LatLng,
                  val end: LatLng,
                  val onSuccess: ((Route) -> Unit))
    : DialogFragment() {
    var mColor: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_route, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 不显示默认标题栏
        dialog.requestWindowFeature(STYLE_NO_TITLE)
        // 默认宽度为10
        editWidth.setText("10")
        // 设置颜色相关事件
        if (collectionTypeManager.getColorList().isNotEmpty()) {
            val list = collectionTypeManager.getColorList()
            // 默认显示第一种颜色
            mColor = collectionTypeManager.getColorList()[0]
            txtColor.text = mColor
            // 点击选择颜色
            txtColor.setOnClickListener {
                // 弹出选择颜色对话框
                AlertDialog.Builder(mContext).setTitle("颜色")
                        // 将可选颜色列出
                        .setItems(list.toTypedArray(), {
                            dialogInterface, i ->
                            // 获取选择的颜色
                            mColor = list[i]
                            // 显示选择的颜色
                            txtColor.text = mColor
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
            if (editWidth.text.toString().toFloatOrNull() == null) {
                StringUtil.showToast(mContext, "请输入宽度")
                return@setOnClickListener
            }
            var color: Int? = null
            if (mColor == null) {
                // 若未设置颜色
                if (collectionTypeManager.getColorList().isNotEmpty()) {
                    // 设置为默认颜色
                    mColor = collectionTypeManager.getColorList()[0]
                } else {
                    return@setOnClickListener
                }
            }
            // 获取选择的颜色代码
            color = collectionTypeManager.getColor(mColor!!)
            // 生成路线
            val route = Route(start, end,
                    editTitle.text.toString(),
                    color!!,
                    editWidth.text.toString().toFloatOrNull() ?: 10f)
            // 添加路线
            routeManager.insertToCloud(route,
                    success = {
                        user, id ->
                        // 添加成功
                        StringUtil.showToast(mContext, "添加成功")
                        route.cloudId = id
                        onSuccess(route)
                        dismiss()
                    },
                    error = routeManager.defaultError(mContext),
                    cloudError = routeManager.defaultCloudError(mContext))
        }
        // 取消
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}