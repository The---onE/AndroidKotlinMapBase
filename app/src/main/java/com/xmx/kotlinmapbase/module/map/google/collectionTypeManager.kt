package com.xmx.kotlinmapbase.module.map.google

import com.xmx.kotlinmapbase.R
import java.util.*

/**
 * Created by The_onE on 2017/3/1.
 * 收藏类型管理，用于将字符串与图标对应，单例对象
 */
object collectionTypeManager {
    val map = LinkedHashMap<String, Int>()

    init {
        map.put("Common", R.drawable.collection)
        map.put("Special", R.drawable.selected)
        map.put("1", R.drawable.poi_marker_1)
        map.put("2", R.drawable.poi_marker_2)
        map.put("3", R.drawable.poi_marker_3)
        map.put("4", R.drawable.poi_marker_4)
        map.put("5", R.drawable.poi_marker_5)
        map.put("6", R.drawable.poi_marker_6)
        map.put("7", R.drawable.poi_marker_7)
        map.put("8", R.drawable.poi_marker_8)
        map.put("9", R.drawable.poi_marker_9)
        map.put("10", R.drawable.poi_marker_10)
        map.put("A", R.drawable.icon_marka)
        map.put("B", R.drawable.icon_markb)
        map.put("C", R.drawable.icon_markc)
        map.put("D", R.drawable.icon_markd)
        map.put("E", R.drawable.icon_marke)
        map.put("F", R.drawable.icon_markf)
        map.put("G", R.drawable.icon_markg)
        map.put("H", R.drawable.icon_markh)
        map.put("I", R.drawable.icon_marki)
        map.put("J", R.drawable.icon_markj)
    }

    /**
     * 根据类型名称获取图标ID
     * @param[type] 类型名称
     * @return 图标drawable ID
     */
    fun getIconId(type: String): Int? = map[type]

    /**
     * 获取类型名称列表
     * @return 类型名称列表
     */
    fun getTypeList(): List<String> = map.keys.toList()
}
