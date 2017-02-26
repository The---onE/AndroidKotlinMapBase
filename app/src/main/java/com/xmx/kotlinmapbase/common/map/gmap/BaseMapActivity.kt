package com.xmx.kotlinmapbase.common.map.gmap

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.UiSettings
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
}