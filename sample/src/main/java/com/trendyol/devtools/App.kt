package com.trendyol.devtools

import android.app.Application
import com.trendyol.devtools.internal.debugmenu.DebugActionItem

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TrendyolDevTools.init(this)
        TrendyolDevTools.addDebugActionItems(
            listOf(
                DebugActionItem("Toggle SSL Pinning"),
                DebugActionItem("Inspect")
            )
        )
    }
}
