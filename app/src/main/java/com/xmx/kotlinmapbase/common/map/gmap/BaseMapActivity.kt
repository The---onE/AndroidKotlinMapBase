package com.xmx.kotlinmapbase.common.map.gmap

import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.xmx.kotlinmapbase.base.activity.BaseTempActivity

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
}