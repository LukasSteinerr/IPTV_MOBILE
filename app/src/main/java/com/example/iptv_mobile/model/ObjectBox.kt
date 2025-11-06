package com.example.iptv_mobile.model

import android.content.Context
import io.objectbox.BoxStore

object ObjectBox {

    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = com.example.iptv_mobile.MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}