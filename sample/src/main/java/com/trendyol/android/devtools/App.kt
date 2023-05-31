package com.trendyol.android.devtools

import android.app.Application
import android.widget.Toast
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
        DebugMenu.addDebugActionItems(
            listOf(
                object : DebugActionItem.Switchable(
                    text = "Toggle SSL Pinning",
                    iconDrawable = android.R.drawable.ic_lock_lock,
                    description = "Toggles SSL pinning",
                ) {

                    override fun onCheckboxStatusChanged(isChecked: Boolean): Boolean {
                        val status = if (isChecked) "active" else "disabled"
                        Toast.makeText(this@App, "SSL Pinning is $status", Toast.LENGTH_SHORT).show()
                        return isChecked
                    }
                },
                object : DebugActionItem.Clickable(
                    text = "Clickable",
                    iconDrawable = android.R.drawable.ic_dialog_alert,
                    description = "Opens when clicked",
                ) {

                    override fun onClickItem() {
                        Toast.makeText(this@App, "Clicked!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        )

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
