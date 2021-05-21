package com.trendyol.devtools

import android.app.Application
import android.widget.Toast

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TrendyolDevTools.init(this)
    }
}
