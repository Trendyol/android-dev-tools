package com.trendyol.android.devtools

import android.app.Application
import com.trendyol.android.devtools.analyticslogger.AnalyticsLogger
import com.trendyol.android.devtools.autofillservice.AutofillService
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.DebugMenu
import com.trendyol.devtools.environmentmanager.EnvironmentManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Environment Manager
        EnvironmentManager.init(this)

        // Debug Menu
        DebugMenu.init(this)
        DebugMenu.addDebugAction(DebugActionItem("Toggle SSL Pinning"))

        // Autofill Service
        AutofillService.Builder(this)
            .withFilePath("autofill.json")
            .build()

        AnalyticsLogger.init(this)
    }
}
