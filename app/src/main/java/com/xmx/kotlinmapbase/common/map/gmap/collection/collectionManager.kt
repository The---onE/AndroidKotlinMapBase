package com.xmx.kotlinmapbase.common.map.gmap.collection

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.data.DataConstants
import com.xmx.kotlinmapbase.common.data.cloud.BaseCloudEntityManager
import com.xmx.kotlinmapbase.utils.ExceptionUtil

/**
 * Created by The_onE on 2017/2/28.
 * 收藏管理器，单例对象
 */
object collectionManager : BaseCloudEntityManager<Collection>() {
    init {
        tableName = "GooglePOI" // 表名
        entityTemplate = Collection(LatLng(0.0, 0.0), "", "", "") // 实体模版
        userField = "User" // 用户字段
    }

    /**
     * 默认的错误处理
     * @param[context] 当前上下文
     * @return 用于管理器中error参数的错误处理函数
     */
    fun defaultError(context: Context?): (Int) -> Unit {
        return {
            e ->
            when (e) {
                DataConstants.NOT_INIT -> Toast.makeText(context, R.string.failure, Toast.LENGTH_SHORT).show()
                DataConstants.NOT_LOGGED_IN -> Toast.makeText(context, R.string.not_loggedin, Toast.LENGTH_SHORT).show()
                DataConstants.CHECK_LOGIN_ERROR -> Toast.makeText(context, R.string.cannot_check_login, Toast.LENGTH_SHORT).show()
                DataConstants.NOT_RELATED_USER -> Toast.makeText(context, R.string.not_related_user, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 默认的网络错误处理
     * @param[context] 当前上下文
     * @return 用于管理器中cloudError参数的网络错误处理函数
     */
    fun defaultCloudError(context: Context?): (Exception) -> Unit {
        return {
            e ->
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
            ExceptionUtil.normalException(e, context)
        }
    }
}