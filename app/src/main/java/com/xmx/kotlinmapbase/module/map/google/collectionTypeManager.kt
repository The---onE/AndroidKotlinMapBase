package com.xmx.kotlinmapbase.module.map.google

import android.graphics.Color
import com.xmx.kotlinmapbase.R
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by The_onE on 2017/3/1.
 * 收藏类型管理，用于将字符串与图标对应，单例对象
 */
object collectionTypeManager {
    val typeMap = LinkedHashMap<String, Int>()
    
    val colorMap = LinkedHashMap<String, Int>()

    init {
        typeMap.apply {
            put("一般", R.drawable.collection)
            put("特殊", R.drawable.selected)
            put("1", R.drawable.poi_marker_1)
            put("2", R.drawable.poi_marker_2)
            put("3", R.drawable.poi_marker_3)
            put("4", R.drawable.poi_marker_4)
            put("5", R.drawable.poi_marker_5)
            put("6", R.drawable.poi_marker_6)
            put("7", R.drawable.poi_marker_7)
            put("8", R.drawable.poi_marker_8)
            put("9", R.drawable.poi_marker_9)
            put("10", R.drawable.poi_marker_10)
            put("A", R.drawable.icon_marka)
            put("B", R.drawable.icon_markb)
            put("C", R.drawable.icon_markc)
            put("D", R.drawable.icon_markd)
            put("E", R.drawable.icon_marke)
            put("F", R.drawable.icon_markf)
            put("G", R.drawable.icon_markg)
            put("H", R.drawable.icon_markh)
            put("I", R.drawable.icon_marki)
            put("J", R.drawable.icon_markj)
        }
        
        colorMap.apply {
            put("黑色", Color.BLACK)
            put("白色", Color.WHITE)
            put("红色", Color.RED)
            put("蓝色", Color.BLUE)
            put("绿色", Color.GREEN)
            put("黄色", Color.YELLOW)
            put("青色", Color.CYAN)
            put("品红色", Color.MAGENTA)
            put("灰色", Color.GRAY)
            put("暗灰色", Color.DKGRAY)
            put("亮灰色", Color.LTGRAY)
        }
    }

    /**
     * 根据类型名称获取图标ID
     * @param[type] 类型名称
     * @return 图标drawable ID
     */
    fun getIconId(type: String): Int? = typeMap[type]

    /**
     * 获取类型名称列表
     * @return 类型名称列表
     */
    fun getTypeList(): List<String> = typeMap.keys.toList()

    /**
     * 根据颜色名称获取颜色代码
     * @param[color] 颜色名称
     * @return 颜色代码
     */
    fun getColor(color: String): Int? = colorMap[color]

    /**
     * 获取颜色名称列表
     * @return 颜色名称列表
     */
    fun getColorList(): List<String> = colorMap.keys.toList()
}
