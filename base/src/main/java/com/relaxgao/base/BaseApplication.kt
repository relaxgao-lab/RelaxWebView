package com.relaxgao.base

import android.app.Application

open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        sApplication = this
    }

    companion object {
        var sApplication: Application? = null
    }
}