package com.example.iptv_mobile.di

import android.app.Application
import com.example.iptv_mobile.model.ObjectBox as ModelObjectBox

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ModelObjectBox.init(this)
    }
}