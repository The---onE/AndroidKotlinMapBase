package com.xmx.kotlinmapbase.common.map.gmap.route

import android.content.ContentValues
import android.database.Cursor
import com.avos.avoscloud.AVObject
import com.google.android.gms.maps.model.LatLng
import com.xmx.kotlinmapbase.common.data.cloud.ICloudEntity
import com.xmx.kotlinmapbase.common.data.sql.ISQLEntity
import com.xmx.kotlinmapbase.common.data.sync.ISyncEntity
import java.util.*

/**
 * Created by The_onE on 2017/3/2.
 * 自定义路径
 * @property[mStart] 起点位置
 * @property[mEnd] 终点位置
 * @property[mTitle] 标题
 * @property[mColor] 颜色
 * @property[mWidth] 宽度
 */
class Route(var mStart: LatLng,
            var mEnd: LatLng,
            var mTitle: String,
            var mColor: Int,
            var mWidth: Float): ISyncEntity {

    var mId: Long = -1
    override var cloudId: String = ""
    var mTime: Date = Date()

    override fun tableFields(): String {
        return "ID integer not null primary key autoincrement, " + // 0
                "CLOUD_ID text, " + // 1
                "StartLatitude real not null, " + // 2
                "StartLongitude real not null, " + // 3
                "EndLatitude real not null, " + // 4
                "EndLongitude real not null, " + // 5
                "Title text, " + // 6
                "Color integer not null default(-16777216)," + // 7 默认为黑色
                "Width real, " + // 8
                "Time integer not null default(0)" // 9
    }

    override fun getContent(): ContentValues {
        val content = ContentValues()
        if (mId > 0) {
            content.put("ID", mId)
        }
        if (cloudId.isNotBlank()) {
            content.put("CLOUD_ID", cloudId)
        }
        content.put("StartLatitude", mStart.latitude)
        content.put("StartLongitude", mStart.longitude)
        content.put("EndLatitude", mEnd.latitude)
        content.put("EndLongitude", mEnd.longitude)
        content.put("Title", mTitle)
        content.put("Color", mColor)
        content.put("Width", mColor)
        content.put("Time", mTime.time)
        return content
    }

    override fun convertToEntity(c: Cursor): ISQLEntity {
        val id = c.getLong(0)
        val cloudId = c.getString(1)
        val sLatitude = c.getDouble(2)
        val sLongitude = c.getDouble(3)
        val eLatitude = c.getDouble(4)
        val eLongitude = c.getDouble(5)
        val title = c.getString(6)
        val color = c.getInt(7)
        val width = c.getFloat(8)
        val time = Date(c.getLong(9))

        val entity = Route(LatLng(sLatitude, sLongitude),
                LatLng(eLatitude, eLongitude),
                title,
                color,
                width)
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
        obj.put("startLatitude", mStart.latitude)
        obj.put("startLongitude", mStart.longitude)
        obj.put("endLatitude", mEnd.latitude)
        obj.put("endLongitude", mEnd.longitude)
        obj.put("title", mTitle)
        obj.put("color", mColor)
        obj.put("width", mWidth.toDouble())
        obj.put("time", mTime)

        return obj
    }

    override fun convertToEntity(obj: AVObject): ICloudEntity {
        val cloudId = obj.objectId
        val id = obj.getLong("id")
        val sLatitude = obj.getDouble("startLatitude")
        val sLongitude = obj.getDouble("startLongitude")
        val eLatitude = obj.getDouble("endLatitude")
        val eLongitude = obj.getDouble("endLongitude")
        val title = obj.getString("title")
        val color = obj.getInt("color")
        val width = obj.getDouble("width")
        val time = obj.getDate("time")

        val entity = Route(LatLng(sLatitude, sLongitude),
                LatLng(eLatitude, eLongitude),
                title,
                color,
                width.toFloat())
        entity.mId = id
        entity.cloudId = cloudId
        entity.mTime = time

        return entity
    }
}