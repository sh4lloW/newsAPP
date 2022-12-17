package com.example.newsapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import org.litepal.LitePalApplication

class MyApplication: LitePalApplication() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val KEY = "82e0e07c02b29cfa42ae2d153c2924e1"
    }

    override fun onCreate() {
        super.onCreate()
        context = baseContext
    }
}