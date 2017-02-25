package com.xmx.kotlinmapbase.core.activity

import android.os.Bundle
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.base.activity.BaseSplashActivity
import com.xmx.kotlinmapbase.common.user.UserConstants
import com.xmx.kotlinmapbase.common.user.UserData
import com.xmx.kotlinmapbase.common.user.userManager
import com.xmx.kotlinmapbase.core.CoreConstants
import com.xmx.kotlinmapbase.utils.ExceptionUtil
import com.xmx.kotlinmapbase.utils.Timer
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by The_onE on 2017/2/15.
 * 应用启动页，一定时间后自动或点击按钮跳转至主Activity
 */
class SplashActivity : BaseSplashActivity() {
    // 定时器，一定时间后跳转主Activity
    val timer: Timer by lazy {
        Timer {
            jumpToMainActivity()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_splash)
    }

    override fun setListener() {
        // 跳过等待，直接跳转
        btnSkip.setOnClickListener {
            timer.stop()
            timer.execute()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 设置定时器在一定时间后跳转
        timer.start(CoreConstants.SPLASH_TIME, once = true)
        // 自动登录
        userManager.autoLogin(
                success = {
                    data: UserData ->
                },
                error = {
                    e: Int ->
                    when (e) {
                        UserConstants.NOT_LOGGED_IN -> showToast(R.string.not_loggedin)
                        UserConstants.USERNAME_ERROR -> showToast(R.string.username_error)
                        UserConstants.CHECKSUM_ERROR -> showToast(R.string.not_loggedin)
                        UserConstants.CANNOT_CHECK_LOGIN -> showToast(R.string.cannot_check_login)
                    }
                },
                cloudError = {
                    e ->
                    showToast(R.string.network_error)
                    ExceptionUtil.normalException(e, this)
                }
        )
    }
}
