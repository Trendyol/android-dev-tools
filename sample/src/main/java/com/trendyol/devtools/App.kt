package com.trendyol.devtools

import android.app.Application
import com.trendyol.devtools.autofill.api.AutofillData
import com.trendyol.devtools.autofill.api.AutofillService
import com.trendyol.devtools.internal.debugmenu.DebugActionItem

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TrendyolDevTools.init(this)
        TrendyolDevTools.addDebugAction(DebugActionItem("Toggle SSL Pinning"))

        AutofillService.Builder(this)
            .withAutoFillData(
                listOf(
                    AutofillData("test@trendyol.com", "123456", "Test for meal"),
                    AutofillData("guest@trendyol.com", "123456", "Test for black wallet"),
                    AutofillData("dev@trendyol.com", "123456", "Test for checkout"),
                )
            )
            .build()
    }
}
