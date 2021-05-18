package com.trendyol.devtools

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TrendyolDevTools.init(this)
    }
}
