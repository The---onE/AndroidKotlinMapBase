package com.xmx.kotlinmapbase.module.map.google

import android.app.Activity
import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
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
    var mGMap : GoogleMap? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_gmap)
        // 加载地图对象，设置回调
        mMapFragment.getMapAsync(OnMapReadyCallback {
            // 谷歌地图对象句柄
            map ->
            mGMap = map
            // 自定义地图显示效果
            val tokyo = LatLng(35.7, 139.7)
            mGMap?.addMarker(MarkerOptions().position(tokyo).title("Marker in Tokyo"))
            mGMap?.moveCamera(CameraUpdateFactory.newLatLng(tokyo))
        })
    }

    override fun setListener() {
    }

    override fun processLogic(savedInstanceState: Bundle?) {
    }
}
