package com.xmx.kotlinmapbase.common.map.gmap

import android.graphics.Color
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.*
import com.xmx.kotlinmapbase.base.activity.BaseTempActivity
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions


/**
 * Created by The_onE on 2017/2/26.
 * 谷歌地图基类
 * 在initView中调用 地图Fragment.getMapAsync({ setMap(it) })
 */
abstract class BaseMapActivity : BaseTempActivity() {
    // 谷歌地图对象句柄
    var mGMap: GoogleMap? = null
    // 地图UI设置
    var mMapUi: UiSettings? = null

    // 当前选定位置
    var selectedPosition: LatLng? = null
    var selectedTitle: String? = null
    var selectedMarker: Marker? = null

    // 副选定位置
    var deputyPosition: LatLng? = null
    var deputyMarker: Marker? = null
    var linkPolyline: Polyline? = null

    /**
     * 从地图Fragment控件获取地图对象
     * 调用此方法才能初始化地图
     * @param[map] 地图对象
     */
    fun setMap(map: GoogleMap) {
        // 地图对象
        mGMap = map
        // UI设置
        mMapUi = mGMap?.uiSettings

        // 设置地图属性
        mGMap?.apply {
            initMap(this)
            setMapListener(this)
        }

        // 设置UI
        mMapUi?.apply {
            setMapUi(this)
        }
    }

    /**
     * 初始化地图，设置地图属性接口
     * @param[map] 地图对象
     */
    abstract fun initMap(map: GoogleMap?)

    /**
     * 设置地图事件监听接口
     * @param[map] 地图对象
     */
    abstract fun setMapListener(map: GoogleMap?)

    /**
     * 设置地图UI属性接口
     * @param[ui] 地图UI属性
     */
    abstract fun setMapUi(ui: UiSettings?)

    /**
     * 设置选中的点，移除之前选中点的标记，添加新标记
     * @param[description] 选中点的描述
     * @param[position] 选中点的位置
     */
    fun setSelectedPoint(description: String, position: LatLng) {
        mGMap?.apply {
            // 设置当前点击位置
            selectedPosition = position
            selectedTitle = description
            // 移除上次标记后添加当前点击位置标记
            selectedMarker?.remove()
            val latitude = (Math.round(position.latitude * 100000)).toFloat() / 100000
            val longitude = (Math.round(position.longitude * 100000)).toFloat() / 100000
            val des = description.replace('\n', ' ')
            selectedMarker = addMarker(MarkerOptions().position(position)
                    .title("$des($latitude,$longitude)"))
            // 添加主位置和副位置的连接线
            deputyPosition?.apply {
                addLinkPolyLine()
            }
        }
    }

    /**
     * 设置POI为选中的点
     * @param[poi] 要设置的POI
     */
    fun setSelectedPoint(poi: PointOfInterest) {
        setSelectedPoint(poi.name, poi.latLng)
    }

    /**
     * 设置使用地点选取器选取的点为选中的点
     * @param[place] 地点选取器选取的点
     */
    fun setSelectedPoint(place: Place) {
        setSelectedPoint(place.name.toString(), place.latLng)
    }

    /**
     * 设置副位置
     * @param[description] 描述
     * @param[position] 位置
     * @param[iconId] 标记图标
     */
    fun setDeputyPoint(description: String, position: LatLng, iconId: Int) {
        mGMap?.apply {
            // 设置位置
            deputyPosition = position
            // 移除上次标记后添加新位置标记
            deputyMarker?.remove()
            deputyMarker = addMarker(MarkerOptions().position(position)
                    .title(description)
                    .icon(BitmapDescriptorFactory.fromResource(iconId))
                    .anchor(0.5f, 0.5f))
            // 添加主位置和副位置的连接线
            selectedPosition?.apply {
                addLinkPolyLine()
            }
        }
    }

    /**
     * 添加标记
     * @param[position] 位置
     * @param[title] 标题
     * @param[iconId] 图标drawable ID
     * @param[content] 内容
     * @return 生成的标记
     */
    fun addMarker(position: LatLng, title: String, iconId: Int, content: String?): Marker? {
        return mGMap?.addMarker(MarkerOptions()
                .position(position)
                .title(title)
                .snippet(content)
                .icon(BitmapDescriptorFactory.fromResource(iconId))
        )
    }

    /**
     * 添加主位置与副位置的连接线
     */
    fun addLinkPolyLine() {
        linkPolyline?.remove()
        selectedPosition?.let {
            deputyPosition?.let {
                val rectOptions = PolylineOptions()
                        .add(selectedPosition)
                        .add(deputyPosition)
                        .geodesic(true) // 绘制为测地线，非直线
                // 默认为黑色，宽度为10.0
                linkPolyline = mGMap?.addPolyline(rectOptions)
            }
        }
    }

    /**
     * 添加一条从起点到终点的测地线
     * @param[start] 起点
     * @param[end] 终点
     * @param[color] 颜色
     * @param[width] 宽度
     */
    fun addRoute(start: LatLng, end: LatLng,
                 color: Int,
                 width: Float): Polyline? {
        val rectOptions = PolylineOptions()
                .add(start)
                .add(end)
                .color(color)
                .width(width)
                .geodesic(true) // 绘制为测地线，非直线
                .clickable(true) // 可以被点击
        return mGMap?.addPolyline(rectOptions)
    }
}