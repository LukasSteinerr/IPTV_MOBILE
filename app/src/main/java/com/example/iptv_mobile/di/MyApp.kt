package com.example.iptv_mobile.di

import android.app.Application
import android.util.Log
import com.example.iptv_mobile.BuildConfig
import com.example.iptv_mobile.model.ObjectBox
import io.objectbox.android.Admin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        ObjectBox.init(this)

        // Start ObjectBox Admin in debug builds
        if (BuildConfig.DEBUG) {
            val started = Admin(ObjectBox.boxStore).start(this)
            Log.i("ObjectBoxAdmin", "Started: $started")
        }
    }
}