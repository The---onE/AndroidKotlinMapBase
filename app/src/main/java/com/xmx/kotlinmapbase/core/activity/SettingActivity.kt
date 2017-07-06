package com.xmx.kotlinmapbase.core.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.base.activity.BaseTempActivity
import com.xmx.kotlinmapbase.common.data.dataManager
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by The_onE on 2016/9/17.
 * 设置Activity
 */
class SettingActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_setting)

        tvCollectionTable.text = dataManager.collectionTableName
        tvRouteTable.text = dataManager.routeTableName
    }

    override fun setListener() {
        layoutCollectionTable.setOnClickListener {
            val typeEdit = EditText(this@SettingActivity)
            typeEdit.setTextColor(Color.BLACK)
            typeEdit.textSize = 24f
            typeEdit.setText(dataManager.collectionTableName)
            AlertDialog.Builder(this@SettingActivity)
                    .setTitle("收藏表名")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(typeEdit)
                    .setPositiveButton("确定") { _, _ ->
                        val type = typeEdit.text.toString().trim()
                        if (type.isNotBlank()) {
                            dataManager.collectionTableName = type
                        } else {
                            showToast("表名不能为空")
                        }
                        tvCollectionTable.text = type
                        showToast("更改成功")
                    }
                    .setNegativeButton("取消", null).show()
        }

        layoutRouteTable.setOnClickListener {
            val typeEdit = EditText(this@SettingActivity)
            typeEdit.setTextColor(Color.BLACK)
            typeEdit.textSize = 24f
            typeEdit.setText(dataManager.routeTableName)
            AlertDialog.Builder(this@SettingActivity)
                    .setTitle("路线表名")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(typeEdit)
                    .setPositiveButton("确定") { _, _ ->
                        val type = typeEdit.text.toString().trim()
                        if (type.isNotBlank()) {
                            dataManager.routeTableName = type
                        } else {
                            showToast("表名不能为空")
                        }
                        tvRouteTable.text = type
                        showToast("更改成功")
                    }
                    .setNegativeButton("取消", null).show()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
