package com.example.iptv_mobile

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}