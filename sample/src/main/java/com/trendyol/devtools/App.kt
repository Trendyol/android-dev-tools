package com.trendyol.android.devtools

import android.app.Application
import com.trendyol.android.devtools.autofill.AutofillService
import com.trendyol.android.devtools.internal.debugmenu.DebugActionItem

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
