package com.xmx.kotlinmapbase.Fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.kotlinmapbase.Log.OperationLogActivity
import com.xmx.kotlinmapbase.R
import com.xmx.kotlinmapbase.Tools.FragmentBase.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by The_onE on 2017/1/18.
 */
class HomeFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater!!.inflate(R.layout.fragment_home, container, false);
    }

    override fun initView(view: View) {

    }

    override fun setListener(view: View) {
        // 查看日志
        btnShowOperationLog.setOnClickListener {
            startActivity(OperationLogActivity::class.java)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}