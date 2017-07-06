package com.xmx.kotlinmapbase.common.map.gmap.collection

import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.common.data.cloud.BaseCloudEntityManager

/**
 * Created by The_onE on 2017/2/28.
 * 收藏管理器，单例对象
 */
object collectionManager : BaseCloudEntityManager<Collection>(), ICollectionManager<Collection> {
    override fun changeTable(name: String) {
        if (name.isNotBlank()) {
            tableName = name
        }
    }

    init {
        tableName = "GooglePOI" // 表名
        entityTemplate = Collection(LatLng(0.0, 0.0), "", "", "") // 实体模版
        userField = "User" // 用户字段
    }
}