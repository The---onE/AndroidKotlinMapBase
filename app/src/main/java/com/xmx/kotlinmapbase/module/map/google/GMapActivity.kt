package com.xmx.kotlinmapbase.module.map.google

import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.map.gmap.BaseMapActivity
import kotlinx.android.synthetic.main.activity_gmap.*

class GMapActivity : BaseMapActivity() {
    // 谷歌地图布局Fragment
    val mMapFragment by lazy {
        fragmentManager.findFragmentById(R.id.map) as MapFragment
    }

    // 当前选定位置
    var selectedPosition: LatLng? = null
    var selectedMarker: Marker? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_gmap)
        // 加载地图对象，设置回调
        mMapFragment.getMapAsync({ setMap(it) })
    }

    override fun initMap(map: GoogleMap?) {
        map?.apply {
            // 开启定位
            isMyLocationEnabled = true

            // 地图中心
            val center = LatLng(35.7, 139.7)
            moveCamera(CameraUpdateFactory.newLatLngZoom(center, 6f))
        }
    }

    override fun setMapListener(map: GoogleMap?) {
        map?.apply {
            // 点击事件
            setOnMapClickListener {
                position ->
                // 设置当前点击位置
                selectedPosition = position
                // 移除上次标记后添加当前点击位置标记
                selectedMarker?.remove()
                selectedMarker = addMarker(MarkerOptions().position(position)
                        .title("Selected:${position.toString()}"))
            }
        }
    }

    override fun setMapUi(ui: UiSettings?) {
        ui?.apply {
            // 显示缩放按钮
            isZoomControlsEnabled = true
            // 开启室内地图
            isIndoorLevelPickerEnabled = true
            // 开启地图工具栏
            isMapToolbarEnabled = true
        }
    }

    override fun setListener() {
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
