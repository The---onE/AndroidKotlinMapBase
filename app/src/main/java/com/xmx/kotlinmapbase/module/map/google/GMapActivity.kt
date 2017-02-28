package com.xmx.kotlinmapbase.module.map.google

import android.os.Bundle
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.map.gmap.BaseMapActivity
import kotlinx.android.synthetic.main.activity_gmap.*
import android.content.Intent
import android.view.View.VISIBLE

class GMapActivity : BaseMapActivity() {
    // 谷歌地图布局Fragment
    val mMapFragment by lazy {
        fragmentManager.findFragmentById(R.id.map) as MapFragment
    }

    // 选点请求
    val PLACE_PICKER_REQUEST = 1

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
            selectedPosition = center
        }
    }

    override fun setMapListener(map: GoogleMap?) {
        map?.apply {
            // 点击事件
            setOnMapClickListener {
                position ->
                setSelectedPoint("未知", position)
                btnCollect.visibility = VISIBLE
            }
            // 官方POI点击事件
            setOnPoiClickListener {
                poi ->
                setSelectedPoint(poi)
                btnCollect.visibility = VISIBLE
                showToast(poi.name)
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
        // 选点
        btnPicker.setOnClickListener {
            // 打开系统地点选取器在选定点附近选点
            // 需要在 https://console.developers.google.com/apis/ 启用 Google Places API for Android
            val intent = PlacePicker.IntentBuilder()
                    .setLatLngBounds(LatLngBounds.builder().include(selectedPosition).build())
                    .build(this)
            startActivityForResult(intent, PLACE_PICKER_REQUEST)
        }

        // 收藏
        btnCollect.setOnClickListener {
            selectedPosition?.apply {
                // 显示收藏对话框
                val dialog = CollectDialog(this@GMapActivity, this, selectedTitle)
                dialog.show(fragmentManager, "collect")
            }
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // 选点成功
                val place = PlacePicker.getPlace(this, data)
                setSelectedPoint(place)
                btnCollect.visibility = VISIBLE
                showToast(place.name.toString())
            }
        }
    }
}
