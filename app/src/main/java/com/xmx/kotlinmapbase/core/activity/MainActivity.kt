package com.xmx.kotlinmapbase.core.activity

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.xmx.kotlinmapbase.core.CoreConstants
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.base.activity.BaseActivity
import com.xmx.kotlinmapbase.core.HomePagerAdapter
import com.xmx.kotlinmapbase.core.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.*

/**
 * Created by The_onE on 2017/2/15.
 * 主Activity，利用Fragment展示所有程序内容
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    // 定位权限请求
    private val LOCATION_REQUEST = 1

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)

        // 初始化工具栏
        setSupportActionBar(toolbar)

        // 初始化侧滑菜单
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        // 各标签页Fragment
        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())
        fragments.add(UserFragment())

        // 各标签页标题
        val titles = ArrayList<String>()
        titles.add("首页")
        titles.add("用户")

        // 设置ViewPager中的标签页
        viewPager.adapter = HomePagerAdapter(supportFragmentManager, fragments, titles)
        // 设置标签页底部选项卡
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun setListener() {
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 请求定位权限
        checkLocalPhonePermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST)
        checkOpsPermission(AppOpsManager.OPSTR_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST)
    }

    // 侧滑菜单项点击事件
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // 显示选择的选项卡
        when (item.itemId) {
            R.id.nav_home -> viewPager.currentItem = 0
            R.id.nav_setting -> startActivity(SettingActivity::class.java)
        }
        // 关闭侧边栏
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // 点击返回键时添加二次确认
    private var mExitTime: Long = 0 // 上次按键的时间

    override fun onBackPressed() {
        // 如果侧边栏已打开，则关闭
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return
        }
        // 如果是第一次按键或距离上次按键时间过长，则重新计时
        if (System.currentTimeMillis() - mExitTime > CoreConstants.LONGEST_EXIT_TIME) {
            showToast(R.string.confirm_exit)
            mExitTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("您拒绝了定位的权限，无法使用定位功能，请手动允许该权限！")
                }
            }
        }
    }
}
