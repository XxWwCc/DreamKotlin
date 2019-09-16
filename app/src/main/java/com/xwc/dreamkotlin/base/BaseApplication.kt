package com.xwc.dreamkotlin.base

import android.app.Application
import timber.log.Timber

/**
 * author: xuweichao
 * date: 2019/9/9 15:19
 * desc:
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mApplication = this

        if (true) {
            Timber.plant(Timber.DebugTree())
        } else{
            Timber.plant()
        }
    }

    companion object {
        var mApplication: BaseApplication? = null
    }
}