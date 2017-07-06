package com.xmx.kotlinmapbase.common.map.gmap.route

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.data.DataConstants
import com.xmx.kotlinmapbase.common.data.cloud.BaseCloudEntityManager
import com.xmx.kotlinmapbase.common.map.gmap.collection.IRouteManager
import com.xmx.kotlinmapbase.common.map.gmap.collection.collectionManager
import com.xmx.kotlinmapbase.common.map.gmap.route.Route
import com.xmx.kotlinmapbase.utils.ExceptionUtil

/**
 * Created by The_onE on 2017/2/28.
 * 路线管理器，单例对象
 */
object routeManager : BaseCloudEntityManager<Route>(), IRouteManager<Route> {
    override fun changeTable(name: String) {
        if (name.isNotBlank()) {
            tableName = name
        }
    }

    init {
        tableName = "GoogleRoute" // 表名
        entityTemplate = Route(LatLng(0.0, 0.0), LatLng(0.0, 0.0), "", 0, 0f) // 实体模版
        userField = "User" // 用户字段
    }
}