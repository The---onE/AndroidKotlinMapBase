package com.xmx.kotlinmapbase.module.map.google

import android.app.AlertDialog
import android.os.Bundle
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.map.gmap.BaseMapActivity
import kotlinx.android.synthetic.main.activity_gmap.*
import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.gms.maps.model.Marker
import com.xmx.kotlinmapbase.common.map.gmap.collection.collectionManager
import java.util.*

class GMapActivity : BaseMapActivity() {
    // 谷歌地图布局Fragment
    val mMapFragment by lazy {
        fragmentManager.findFragmentById(R.id.map) as MapFragment
    }

    // 选点请求
    val PLACE_PICKER_REQUEST = 1
    // 收藏地图标记对应Cloud Id
    val markerIdMap = HashMap<Marker, String>()
    // 当前选中的收藏标记
    var collectionMarker: Marker? = null
    // 当前选中的收藏Cloud Id
    var collectionCloudId: String? = null

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
                btnDelete.visibility = GONE
                collectionMarker = null
                collectionCloudId = null
            }
            // 长按事件
            setOnMapLongClickListener {
                position ->
                setDeputyPoint("未知", position, R.drawable.point1)
            }
            // 官方POI点击事件
            setOnPoiClickListener {
                poi ->
                setSelectedPoint(poi)
                btnCollect.visibility = VISIBLE
                btnDelete.visibility = GONE
                collectionMarker = null
                collectionCloudId = null
                showToast(poi.name)
            }
            // 地图标记点击事件
            setOnMarkerClickListener {
                marker ->
                collectionMarker = null
                collectionCloudId = null
                // 若点击的是收藏标记
                val cloudId = markerIdMap[marker]
                cloudId?.apply {
                    setSelectedPoint(marker.title, marker.position)
                    // 显示删除按钮
                    btnCollect.visibility = GONE
                    btnDelete.visibility = VISIBLE
                    collectionMarker = marker
                    collectionCloudId = this
                }
                // 依然显示谷歌地图默认标记信息
                false
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
                val dialog = CollectDialog(this@GMapActivity, this, selectedTitle, {
                    // 将新收藏显示在地图上
                    it.apply {
                        if (collectionTypeManager.getTypeList().isNotEmpty()) {
                            val iconId = collectionTypeManager.getIconId(mType)
                            if (iconId != null) {
                                val marker = addMarker(mPosition, mTitle, iconId, mContent)
                                // 添加收藏对应的Cloud Id
                                marker?.apply { markerIdMap.put(this, cloudId) }
                            }
                        }
                    }
                })
                dialog.show(fragmentManager, "collect")
            }
        }
        // 删除收藏
        btnDelete.setOnClickListener {
            collectionCloudId?.apply {
                AlertDialog.Builder(this@GMapActivity)
                        .setTitle("删除")
                        .setMessage("确定要删除收藏吗")
                        .setPositiveButton(R.string.confirm, {
                            dialog, i ->
                            // 删除收藏
                            collectionManager.deleteFromCloud(this,
                                    success = {
                                        // 删除成功
                                        showToast(R.string.delete_success)
                                        // 移除标记
                                        collectionMarker?.remove()
                                        btnDelete.visibility = GONE
                                        dialog.dismiss()
                                    },
                                    error = collectionManager.defaultError(this@GMapActivity),
                                    cloudError = collectionManager.defaultCloudError(this@GMapActivity)
                            )
                        })
                        .setNegativeButton(R.string.cancel, {
                            dialog, i ->
                            dialog.dismiss()
                        })
                        .show()
            }
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 查询所有收藏
        collectionManager.selectAll(
                success = {
                    list ->
                    list.forEach {
                        // 将收藏显示在地图上
                        it.apply {
                            if (collectionTypeManager.getTypeList().isNotEmpty()) {
                                val iconId = collectionTypeManager.getIconId(mType)
                                if (iconId != null) {
                                    val marker = addMarker(mPosition, mTitle, iconId, mContent)
                                    // 添加收藏对应的Cloud Id
                                    marker?.apply { markerIdMap.put(this, cloudId) }
                                }
                            }
                        }
                    }
                },
                error = collectionManager.defaultError(this),
                cloudError = collectionManager.defaultCloudError(this)
        )
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
