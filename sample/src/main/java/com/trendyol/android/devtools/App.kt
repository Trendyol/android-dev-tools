package com.trendyol.android.devtools

import android.app.Application
import com.trendyol.android.devtools.analyticslogger.AnalyticsLogger
import com.trendyol.android.devtools.autofillservice.AutofillService
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.DebugMenu
import com.trendyol.android.devtools.debugtoast.DebugToast
import com.trendyol.android.devtools.viewinspector.ViewInspector
import com.trendyol.devtools.deeplinklauncher.DeepLinkLauncher
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

        // Analytics Logger
        AnalyticsLogger.init(this)

        // View Inspector
        ViewInspector.init(this)

        // Debug Toast
        DebugToast.init(this)

        // DeepLink Launcher
        DeepLinkLauncher.init(this)
    }
}
