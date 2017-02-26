package com.xmx.kotlinmapbase.module.map.google

import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_gmap.*

class GMapActivity : BaseTempActivity() {
    // 谷歌地图布局Fragment
    val mMapFragment by lazy {
        fragmentManager.findFragmentById(R.id.map) as MapFragment
    }
    // 谷歌地图对象句柄
    var mGMap: GoogleMap? = null
    // 地图UI设置
    var mMapUi: UiSettings? = null

    // 当前选定位置
    var currentPosition: LatLng? = null
    var currentMarker: Marker? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_gmap)
        // 加载地图对象，设置回调
        mMapFragment.getMapAsync(OnMapReadyCallback {
            // 谷歌地图对象句柄
            map ->
            mGMap = map
            // UI设置
            mMapUi = mGMap?.uiSettings

            // 设置地图属性
            mGMap?.apply {
                // 开启定位
                isMyLocationEnabled = true

                // 地图中心
                val center = LatLng(35.7, 139.7)
                moveCamera(CameraUpdateFactory.newLatLngZoom(center, 6f))

                // 点击事件
                setOnMapClickListener {
                    position ->
                    // 设置当前点击位置
                    currentPosition = position
                    // 添加当前点击位置标记
                    currentMarker?.remove()
                    currentMarker = addMarker(MarkerOptions().position(position)
                            .title("Selected:${position.toString()}"))
                }
            }

            // 设置UI
            mMapUi?.apply {
                // 显示缩放按钮
                isZoomControlsEnabled = true
                // 开启室内地图
                isIndoorLevelPickerEnabled = true
                // 开启地图工具栏
                isMapToolbarEnabled = true
            }
        })
    }

    override fun setListener() {
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
