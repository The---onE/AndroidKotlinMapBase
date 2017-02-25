package com.xmx.kotlinmapbase.module.log

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.base.activity.BaseTempActivity
import com.xmx.kotlinmapbase.common.log.LogChangeEvent
import com.xmx.kotlinmapbase.common.log.OperationLog
import com.xmx.kotlinmapbase.common.log.operationLogEntityManager
import com.xmx.kotlinmapbase.common.log.operationLogManager
import kotlinx.android.synthetic.main.activity_operation_log.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList

/**
 * Created by The_onE on 2016/10/3.
 * 查看日志页，显示所有本应用的操作日志
 */
class OperationLogActivity : BaseTempActivity() {

    // 操作日志列表适配器
    private var operationLogAdapter: OperationLogAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_operation_log)
        operationLogAdapter = OperationLogAdapter(this, ArrayList<OperationLog>())
        listOperationLog.adapter = operationLogAdapter
    }

    override fun setListener() {
        // 清空日志
        btnClearLog.setOnClickListener(View.OnClickListener {
            operationLogEntityManager.clearDatabase()
            operationLogManager.updateData()
            operationLogAdapter?.updateList(operationLogManager.data!!)
        })
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        operationLogManager.updateData()
        operationLogAdapter?.updateList(operationLogManager.data!!)
        // 订阅事件
        EventBus.getDefault().register(this)
    }

    /**
     * 订阅操作日志变动事件
     * @param[event] 操作日志变动事件
     */
    @Subscribe
    fun onEvent(event: LogChangeEvent) {
        operationLogManager.updateData()
        operationLogAdapter?.updateList(operationLogManager.data!!)
    }
}
