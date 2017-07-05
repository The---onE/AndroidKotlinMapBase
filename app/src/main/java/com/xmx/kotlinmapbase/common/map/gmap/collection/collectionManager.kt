package com.xmx.kotlinmapbase.common.map.gmap.collection

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.data.DataConstants
import com.xmx.kotlinmapbase.common.data.cloud.BaseCloudEntityManager
import com.xmx.kotlinmapbase.common.map.gmap.ICollectionManager
import com.xmx.kotlinmapbase.utils.ExceptionUtil

/**
 * Created by The_onE on 2017/2/28.
 * 收藏管理器，单例对象
 */
object collectionManager : BaseCloudEntityManager<Collection>(), ICollectionManager<Collection> {
    init {
        tableName = "GooglePOI" // 表名
        entityTemplate = Collection(LatLng(0.0, 0.0), "", "", "") // 实体模版
        userField = "User" // 用户字段
    }
}