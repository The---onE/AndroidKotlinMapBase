package com.xmx.kotlinmapbase.Splash

import android.os.Bundle
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.Tools.ActivityBase.BaseSplashActivity
import com.xmx.kotlinmapbase.Tools.Timer
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseSplashActivity() {

    var timer: Timer? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_splash)
    }

    override fun setListener() {
        btnSkip.setOnClickListener {
            timer?.stop()
            jumpToMainActivity()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        timer = Timer {
            jumpToMainActivity()
        }
        timer!!.start(3000, once = true)
    }
}
