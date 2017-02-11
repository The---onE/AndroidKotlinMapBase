package com.xmx.kotlinmapbase

import android.app.Activity
import android.app.Service
import com.avos.avoscloud.AVOSCloud
import com.xmx.kotlinmapbase.Tools.CrashHandler
import com.xmx.kotlinmapbase.Tools.User.UserManager
import java.util.*

/**
 * Created by The_onE on 2017/1/16.
 * 自定义Application，单例类，初始化各插件
 */

class MyApplication : android.app.Application() {
    // 单例模式
    companion object {
        private var instance: MyApplication? = null
        fun instance(): MyApplication = instance!!
    }

    // 运行中的Activity容器
    private val activityList = LinkedList<Activity>()
    // 运行中的Service容器
    private val serviceList = LinkedList<Service>()

    // 添加Activity到容器中
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    // 添加Service到容器中
    fun addService(service: Service) {
        serviceList.add(service)
    }

    // 退出程序，遍历所有Activity并finish
    fun exit() {
        activityList.forEach { it.finish() }
        serviceList.forEach { it.stopSelf() }
        System.exit(0)
    }

    // 初始化过程
    override fun onCreate() {
        super.onCreate()
        instance = this

        // 注册异常处理器
        val crashHandler = CrashHandler.instance()
        crashHandler.init(this)

        // 初始化LeanCloud
        AVOSCloud.initialize(this, Constants.APP_ID, Constants.APP_KEY)
        // 初始化用户管理器
        UserManager.instance().setContext(this)
    }
}