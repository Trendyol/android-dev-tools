package com.trendyol.devtools

import android.app.Application
import com.trendyol.devtools.autofill.AutofillService
import com.trendyol.devtools.internal.debugmenu.DebugActionItem

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TrendyolDevTools.init(this)
        TrendyolDevTools.addDebugAction(DebugActionItem("Toggle SSL Pinning"))

        AutofillService.Builder(this)
            .withFilePath("autofill.json")
            .build()
    }
}
