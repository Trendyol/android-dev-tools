package com.trendyol.devtools

import android.app.Application
import com.trendyol.devtools.autofill.api.AutofillService
import com.trendyol.devtools.autofill.api.model.LoginEmail
import com.trendyol.devtools.autofill.api.model.LoginPhoneNumber
import com.trendyol.devtools.internal.debugmenu.DebugActionItem

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TrendyolDevTools.init(this)
        TrendyolDevTools.addDebugAction(DebugActionItem("Toggle SSL Pinning"))

        AutofillService.Builder(this)
            .withAutoFillData(
                listOf(
                    LoginEmail("test@trendyol.com", "123456"),
                    LoginEmail("guest@trendyol.com", "123456"),
                    LoginEmail("dev@trendyol.com", "123456"),
                    LoginPhoneNumber("+90 506 643 1212", "123456"),
                )
            )
            .build()
    }
}
