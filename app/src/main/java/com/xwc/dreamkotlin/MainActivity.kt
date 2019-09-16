package com.xwc.dreamkotlin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.NestedScrollView
import android.widget.Toast
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.xwc.dreamkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BaseActivity() {

    private var locationClient: LocationClient? = null

    override fun setLayout(): Int {
        return R.layout.activity_main
    }

    override fun initViewAndData() {
        getPermission()
        initTitle()
        msv_main.showContent()
    }

    private fun initTitle() {
        nsv_main.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { scrollView, _, _, _, _ ->
            val minHeight = 44
            var mAlpha = 0
            var maxHeight = tool_bar.measuredHeight
            if (maxHeight == 0) {
                maxHeight = 450
            }
            if (scrollView != null) {
                mAlpha = when {
                    scrollView.scrollY <= minHeight -> 0
                    scrollView.scrollY > maxHeight -> 255
                    else -> (scrollView.scrollY - minHeight) * 255 / (maxHeight - minHeight)
                }
            }
            when {
                mAlpha <= 0 -> tool_bar.setBackgroundColor(Color.argb(0, 255, 69, 0))
                mAlpha >= 255 -> tool_bar.setBackgroundColor(Color.argb(mAlpha, 255, 69, 0))
                else -> tool_bar.setBackgroundColor(Color.argb(mAlpha, 255, 69, 0))//rgb设置的是titleBar的背景色
            }
        })

        ll_location.setOnClickListener {
            getPermission()
        }
    }

    private fun getPermission() {
        Timber.e("getPermission")
        if (PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Timber.e("got")
            startLocation()
        } else {
            Timber.e("get")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        ToastUtils.showLong("请授予定位权限")
                        return
                    }
                }
                startLocation()
            }
        }
    }

    private fun startLocation() {
        locationClient = LocationClient(this)
        val option = LocationClientOption()
        option.setIsNeedAddress(true)
        locationClient?.locOption = option
        locationClient?.start()
        locationClient?.registerLocationListener(mLocationListener)
    }

    private var mLocationListener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            if (location != null) {
                if (location.city != null && location.city != "") {
                    tv_location.text = location.city
                } else {
                    Toast.makeText(this@MainActivity, "获取定位失败", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "定位失败", Toast.LENGTH_SHORT).show()
            }
            locationClient?.stop()
            locationClient?.unRegisterLocationListener(this)
        }
    }

    companion object {
        fun openActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
