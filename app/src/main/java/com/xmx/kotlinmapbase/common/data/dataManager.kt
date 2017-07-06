package com.xmx.kotlinmapbase.common.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by The_onE on 2016/2/21.
 */
object dataManager {

    var mContext: Context? = null
    var mData: SharedPreferences? = null

    fun setContext(context: Context) {
        mContext = context
        mData = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
    }

    private var version: Long
        get() = mData!!.getLong("version", -1)
        set(value) {
            val editor = mData!!.edit()
            editor.putLong("version", value)
            editor.apply()
        }

    private fun getInt(key: String): Int {
        return mData!!.getInt(key, -1)
    }

    private fun getInt(key: String, def: Int): Int {
        return mData!!.getInt(key, def)
    }

    private fun setInt(key: String, value: Int) {
        val editor = mData!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    private fun getFloat(key: String): Float {
        return mData!!.getFloat(key, -1f)
    }

    private fun getFloat(key: String, def: Float): Float {
        return mData!!.getFloat(key, def)
    }

    private fun setFloat(key: String, value: Float) {
        val editor = mData!!.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    private fun intIncrease(key: String, delta: Int) {
        var i = getInt(key)
        i += delta
        setInt(key, i)
    }

    private fun getLong(key: String, def: Long): Long {
        return mData!!.getLong(key, def)
    }

    private fun setLong(key: String, value: Long) {
        val editor = mData!!.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    private fun getBoolean(key: String, def: Boolean): Boolean {
        return mData!!.getBoolean(key, def)
    }

    private fun setBoolean(key: String, value: Boolean) {
        val editor = mData!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getString(key: String): String {
        return mData!!.getString(key, "")
    }

    private fun getString(key: String, def: String): String {
        return mData!!.getString(key, def)
    }

    private fun setString(key: String, value: String) {
        val editor = mData!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    var collectionTableName: String
        get() = getString("collection_table_name")
        set(name) = setString("collection_table_name", name)

    var routeTableName: String
        get() = getString("route_table_name")
        set(name) = setString("route_table_name", name)
}
