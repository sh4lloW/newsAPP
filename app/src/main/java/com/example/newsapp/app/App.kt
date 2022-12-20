package com.example.newsapp.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var app: App
        lateinit var key: String
        //当前用户
        lateinit var Dangqianuser:String
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        app =this
    }

    fun getApp(): App {
        return app
    }
}
