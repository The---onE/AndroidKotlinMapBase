package com.xmx.kotlinmapbase.module.map.google

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.common.map.gmap.BaseMapActivity
import kotlinx.android.synthetic.main.activity_gmap.*
import android.content.Intent
import android.location.LocationManager
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.xmx.kotlinmapbase.common.map.gmap.collection.Collection
import com.xmx.kotlinmapbase.common.map.gmap.collection.collectionManager
import com.xmx.kotlinmapbase.common.map.gmap.route.Route
import com.xmx.kotlinmapbase.common.map.gmap.route.routeManager
import kotlin.collections.HashMap

class GMapActivity : BaseMapActivity() {
    // 谷歌地图布局Fragment
    val mMapFragment by lazy {
        fragmentManager.findFragmentById(R.id.map) as MapFragment
    }

    // 选点请求
    val PLACE_PICKER_REQUEST = 1
    // 收藏地图标记对应Cloud Id
    val markerCollectionMap = HashMap<Marker, Collection>()
    // 当前选中的收藏标记
    var collectionMarker: Marker? = null

    // 地图绘制线对应路线
    val polylineRouteMap = HashMap<Polyline, Route>()
    // 当前选中地图线
    var selectedPolyline: Polyline? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_gmap)
        // 加载地图对象，设置回调
        mMapFragment.getMapAsync({ setMap(it) })
    }

    override fun initMap(map: GoogleMap?) {
        // 请求一次定位，唤起小米定位权限请求
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        map?.apply {
            // 开启定位
            isMyLocationEnabled = true

            // 地图中心
            val center = LatLng(35.7, 139.7)
            focusPosition(center)
            selectedPosition = center
        }
    }

    override fun setMapListener(map: GoogleMap?) {
        map?.apply {
            // 点击事件
            setOnMapClickListener {
                position ->
                setSelectedPoint("未知", position)
//                focusPosition(position, -1f)
                btnCollect.visibility = VISIBLE
                btnEditCollection.visibility = GONE
                btnEditRoute.visibility = GONE
                collectionMarker = null
            }
            // 长按事件
            setOnMapLongClickListener {
                position ->
                setDeputyPoint("未知", position, R.drawable.point1)
                btnAddRoute.visibility = VISIBLE
                btnEditRoute.visibility = GONE
            }
            // 官方POI点击事件
            setOnPoiClickListener {
                poi ->
                setSelectedPoint(poi)
                focusPosition(poi.latLng, -1f)
                btnCollect.visibility = VISIBLE
                btnEditCollection.visibility = GONE
                btnEditRoute.visibility = GONE
                collectionMarker = null
                showToast(poi.name)
            }
            // 地图标记点击事件
            setOnMarkerClickListener {
                marker ->
                collectionMarker = null
                // 若点击的是收藏标记
                val collection = markerCollectionMap[marker]
                collection?.apply {
                    setSelectedPoint(marker.title, marker.position)
                    focusPosition(marker.position, -1f)
                    // 显示删除按钮
                    btnCollect.visibility = GONE
                    btnEditCollection.visibility = VISIBLE
                    btnEditRoute.visibility = GONE
                    collectionMarker = marker
                }
                // 依然显示谷歌地图默认标记信息
                false
            }
            // 路线点击事件
            setOnPolylineClickListener {
                polyline ->
                val route = polylineRouteMap[polyline]
                route?.apply {
                    // 显示路线信息
                    showToast(mTitle)
                    // 可以删除路线
                    selectedPolyline = polyline
                    btnEditRoute.visibility = VISIBLE
                }
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
                // 弹出添加对话框
                addCollection()
            }
        }
        // 编辑收藏
        btnEditCollection.setOnClickListener {
            val collection = markerCollectionMap[collectionMarker]
            collection?.apply {
                AlertDialog.Builder(this@GMapActivity)
                        .setTitle("编辑")
                        .setMessage("要编辑该收藏吗？")
                        .setPositiveButton(R.string.modify, {
                            _, _ ->
                            // 弹出修改对话框
                            modifyCollection(this)
                        })
                        .setNegativeButton(R.string.delete, {
                            _, _ ->
                            // 弹出删除对话框
                            deleteCollection(this)
                        })
                        .setNeutralButton(R.string.cancel, {
                            dialog, _ ->
                            // 取消编辑
                            dialog.dismiss()
                        })
                        .show()
            }
        }
        // 添加路线
        btnAddRoute.setOnClickListener {
            selectedPosition?.let {
                deputyPosition?.let {
                    // 弹出添加对话框
                    addRoute()
                }
            }
        }
        // 删除路线
        btnEditRoute.setOnClickListener {
            val route = polylineRouteMap[selectedPolyline]
            route?.apply {
                AlertDialog.Builder(this@GMapActivity)
                        .setTitle("编辑")
                        .setMessage("要编辑该路线吗？")
                        .setPositiveButton(R.string.modify, {
                            _, _ ->
                            // 弹出修改对话框
                            modifyRoute(this)
                        })
                        .setNegativeButton(R.string.delete, {
                            _, _ ->
                            // 弹出删除对话框
                            deleteRoute(this)
                        })
                        .setNeutralButton(R.string.cancel, {
                            dialog, _ ->
                            // 取消编辑
                            dialog.dismiss()
                        })
                        .show()
            }
        }
    }

    /**
     * 弹出添加对话框添加收藏
     */
    private fun addCollection() {
        // 显示收藏对话框
        val dialog = CollectDialog(this@GMapActivity, selectedPosition!!, selectedTitle, {
            // 将新收藏显示在地图上
            it.apply {
                if (mapConstantsManager.getTypeList().isNotEmpty()) {
                    val iconId = mapConstantsManager.getIconId(mType)
                    if (iconId != null) {
                        val marker = addMarker(mPosition, mTitle, iconId, mContent)
                        // 添加收藏对应的Cloud Id
                        marker?.apply { markerCollectionMap.put(marker, it) }
                    }
                }
            }
        })
        dialog.show(fragmentManager, "collect")
    }

    /**
     * 弹出修改对话框修改收藏
     * @param[collection] 要修改的收藏
     */
    private fun modifyCollection(collection: Collection) {
        val editDialog = CollectDialog(this@GMapActivity, collection, {
            // 将新收藏显示在地图上
            it.apply {
                if (mapConstantsManager.getTypeList().isNotEmpty()) {
                    val iconId = mapConstantsManager.getIconId(mType)
                    if (iconId != null) {
                        // 移除之前的标记
                        collectionMarker?.remove()
                        // 添加修改后的标记
                        val marker = addMarker(mPosition, mTitle, iconId, mContent)
                        // 添加新的标记与收藏对应
                        marker?.apply { markerCollectionMap.put(marker, it) }
                        collectionMarker = marker
                    }
                }
            }
        })
        editDialog.show(fragmentManager, "editCollection")
    }

    /**
     * 弹出删除提示框删除收藏
     * @param[collection] 要删除的收藏
     */
    private fun deleteCollection(collection: Collection) {
        AlertDialog.Builder(this@GMapActivity)
                .setTitle("删除")
                .setMessage("确定要删除该收藏吗？")
                .setPositiveButton(R.string.confirm, {
                    deleteDialog, _ ->
                    // 删除收藏
                    collectionManager.deleteFromCloud(collection.cloudId,
                            success = {
                                // 删除成功
                                showToast(R.string.delete_success)
                                // 移除标记
                                collectionMarker?.remove()
                                btnEditCollection.visibility = GONE
                                deleteDialog.dismiss()
                            },
                            error = collectionManager.defaultError(this@GMapActivity),
                            cloudError = collectionManager.defaultCloudError(this@GMapActivity)
                    )
                })
                .setNeutralButton(R.string.cancel, {
                    deleteDialog, _ ->
                    // 取消删除
                    deleteDialog.dismiss()
                })
                .show()
    }

    /**
     * 弹出添加对话框添加路线
     */
    private fun addRoute() {
        val dialog = RouteDialog(this, selectedPosition!!, deputyPosition!!, {
            it.apply {
                val polyline = addRoute(mStart, mEnd, mColor, mWidth)
                // 添加绘制线对应的路线
                polyline?.apply { polylineRouteMap.put(polyline, it) }
            }
        })
        dialog.show(fragmentManager, "route")
    }

    /**
     * 弹出修改对话框修改路线
     * @param[route] 要修改的路线
     */
    private fun modifyRoute(route: Route) {
        val editDialog = RouteDialog(this@GMapActivity, route, {
            // 将新收藏显示在地图上
            it.apply {
                // 移除之前的绘制线
                selectedPolyline?.remove()
                // 添加修改后的绘制线
                val polyline = addRoute(mStart, mEnd, mColor, mWidth)
                // 添加新的绘制线与路线对应
                polyline?.apply { polylineRouteMap.put(polyline, it) }
                selectedPolyline = polyline
            }
        })
        editDialog.show(fragmentManager, "editCollection")
    }

    /**
     * 弹出删除提示框删除路线
     * @param[route] 要删除的路线
     */
    private fun deleteRoute(route: Route) {
        AlertDialog.Builder(this@GMapActivity)
                .setTitle("删除")
                .setMessage("确定要删除路线吗")
                .setPositiveButton(R.string.confirm, {
                    dialog, _ ->
                    // 删除收藏
                    routeManager.deleteFromCloud(route.cloudId,
                            success = {
                                // 删除成功
                                showToast(R.string.delete_success)
                                // 移除绘制线
                                selectedPolyline?.remove()
                                btnEditRoute.visibility = GONE
                                dialog.dismiss()
                            },
                            error = collectionManager.defaultError(this@GMapActivity),
                            cloudError = collectionManager.defaultCloudError(this@GMapActivity)
                    )
                })
                .setNeutralButton(R.string.cancel, {
                    dialog, _ ->
                    dialog.dismiss()
                })
                .show()
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 查询所有收藏
        collectionManager.selectAll(
                success = {
                    list ->
                    list.forEach {
                        // 将收藏显示在地图上
                        it.apply {
                            if (mapConstantsManager.getTypeList().isNotEmpty()) {
                                val iconId = mapConstantsManager.getIconId(mType)
                                if (iconId != null) {
                                    val marker = addMarker(mPosition, mTitle, iconId, mContent)
                                    // 添加收藏对应的Cloud Id
                                    marker?.apply { markerCollectionMap.put(marker, it) }
                                }
                            }
                        }
                    }
                },
                error = collectionManager.defaultError(this),
                cloudError = collectionManager.defaultCloudError(this)
        )
        // 查询所有路线
        routeManager.selectAll(
                success = {
                    list ->
                    list.forEach {
                        // 将路线显示在地图上
                        it.apply {
                            val polyline = addRoute(mStart, mEnd, mColor, mWidth)
                            // 添加绘制线对应的路线
                            polyline?.apply { polylineRouteMap.put(polyline, it) }
                        }
                    }
                },
                error = routeManager.defaultError(this),
                cloudError = routeManager.defaultCloudError(this)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // 选点成功
                val place = PlacePicker.getPlace(this, data)
                setSelectedPoint(place)
                focusPosition(place.latLng, 15f)
                btnCollect.visibility = VISIBLE
                showToast(place.name.toString())
            }
        }
    }
}
