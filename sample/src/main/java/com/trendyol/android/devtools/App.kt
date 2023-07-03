package com.trendyol.android.devtools

import android.app.Application
import com.trendyol.android.devtools.analyticslogger.AnalyticsLogger
import com.trendyol.android.devtools.autofillservice.AutofillService
import com.trendyol.android.devtools.debugactionitem.DummyClickDebugActionItem
import com.trendyol.android.devtools.debugactionitem.DummySwitchDebugActionItem
import com.trendyol.android.devtools.debugactionitem.AnalyticsLoggerDebugActionItem
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
        val debugMenuItems = listOf(
            AnalyticsLoggerDebugActionItem(),
            DummyClickDebugActionItem(baseContext),
            DummySwitchDebugActionItem(true),
        )
        DebugMenu.addDebugActionItems(debugMenuItems)
        DebugMenu.addDebugAction(DummySwitchDebugActionItem())

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
        applicationContext.assets.open("deepLinks.json").bufferedReader().use {
            val deepLinkData = it.readText()
            DeepLinkLauncher.importAppDeepLinks(deepLinkData)
        }
    }
}
