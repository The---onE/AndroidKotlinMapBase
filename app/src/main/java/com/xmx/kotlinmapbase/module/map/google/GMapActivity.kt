package com.xmx.kotlinmapbase.module.map.google

import android.os.Bundle
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.map.gmap.BaseMapActivity
import kotlinx.android.synthetic.main.activity_gmap.*
import android.content.Intent

class GMapActivity : BaseMapActivity() {
    // 谷歌地图布局Fragment
    val mMapFragment by lazy {
        fragmentManager.findFragmentById(R.id.map) as MapFragment
    }

    // 当前选定位置
    var selectedPosition: LatLng? = null
    var selectedMarker: Marker? = null

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
                // 设置当前点击位置
                selectedPosition = position
                // 移除上次标记后添加当前点击位置标记
                selectedMarker?.remove()
                selectedMarker = addMarker(MarkerOptions().position(position)
                        .title("Selected:$position"))
            }

            setOnPoiClickListener {
                poi ->
                // 设置当前点击位置
                selectedPosition = poi.latLng
                // 移除上次标记后添加当前点击位置标记
                selectedMarker?.remove()
                selectedMarker = addMarker(MarkerOptions().position(poi.latLng)
                        .title("${poi.name}:${poi.latLng}"))
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
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // 选点成功
                val place = PlacePicker.getPlace(this, data)
                showToast("${place.name}")
                // 设置当前点击位置
                selectedPosition = place.latLng
                // 移除上次标记后添加当前点击位置标记
                selectedMarker?.remove()
                selectedMarker = mGMap?.addMarker(MarkerOptions().position(place.latLng)
                        .title("${place.name}:${place.latLng}"))
            }
        }
    }
}
