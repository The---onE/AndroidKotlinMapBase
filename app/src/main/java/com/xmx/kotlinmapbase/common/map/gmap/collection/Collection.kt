package com.xmx.kotlinmapbase.common.map.gmap.collection

import android.content.ContentValues
import android.database.Cursor

import com.avos.avoscloud.AVObject
import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.common.data.sync.ISyncEntity

import java.util.Date

/**
 * Created by The_onE on 2016/12/1.
 * 收藏点
 * @property[mPosition] 位置
 * @property[mTitle] 标题
 * @property[mType] 类型
 * @property[mContent] 内容描述
 */
class Collection(var mPosition: LatLng,
                 var mTitle: String,
                 var mType: String,
                 var mContent: String)
    : ISyncEntity {

    var mId: Long = -1
    override var cloudId: String = ""
    var mTime: Date = Date()

    override fun tableFields(): String {
        return "ID integer not null primary key autoincrement, " + // 0
                "CLOUD_ID text, " + // 1
                "Latitude real not null, " + // 2
                "Longitude real not null, " + // 3
                "Title text, " + // 4
                "Type text, " + // 5
                "Content text, " + // 6
                "Time integer not null default(0)" // 7
    }

    override fun getContent(): ContentValues {
        val content = ContentValues()
        if (mId > 0) {
            content.put("ID", mId)
        }
        if (cloudId.isNotBlank()) {
            content.put("CLOUD_ID", cloudId)
        }
        content.put("Latitude", mPosition.latitude)
        content.put("Longitude", mPosition.longitude)
        content.put("Title", mTitle)
        content.put("Type", mType)
        content.put("Content", mContent)
        content.put("Time", mTime.time)
        return content
    }

    override fun convertToEntity(c: Cursor): Collection {
        val id = c.getLong(0)
        val cloudId = c.getString(1)
        val latitude = c.getDouble(2)
        val longitude = c.getDouble(3)
        val title = c.getString(4)
        val type = c.getString(5)
        val content = c.getString(6)
        val time = Date(c.getLong(7))

        val entity = Collection(LatLng(latitude, longitude), title, type, content)
        entity.mId = id
        entity.cloudId = cloudId
        entity.mTime = time

        return entity
    }

    override fun getContent(tableName: String): AVObject {
        val obj = AVObject(tableName)
        if (cloudId.isNotBlank()) {
            obj.objectId = cloudId
        }
//        if (mId > 0) {
        obj.put("id", mId)
//        }
        obj.put("latitude", mPosition.latitude)
        obj.put("longitude", mPosition.longitude)
        obj.put("title", mTitle)
        obj.put("type", mType)
        obj.put("content", mContent)
        obj.put("time", mTime)

        return obj
    }

    override fun convertToEntity(obj: AVObject): Collection {
        val cloudId = obj.objectId
        val id = obj.getLong("id")
        val latitude = obj.getDouble("latitude")
        val longitude = obj.getDouble("longitude")
        val title = obj.getString("title")
        val type = obj.getString("type")
        val content = obj.getString("content")
        val time = obj.getDate("time")

        val entity = Collection(LatLng(latitude, longitude), title, type, content)
        entity.mId = id
        entity.cloudId = cloudId
        entity.mTime = time

        return entity
    }

}
