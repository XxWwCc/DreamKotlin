package com.xwc.dreamkotlin

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

/**
 * author: xuweichao
 * date: 2019/9/12 17:45
 * desc: 启动页
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            MainActivity.openActivity(this)
            finish()
        }, 2000)
    }
}