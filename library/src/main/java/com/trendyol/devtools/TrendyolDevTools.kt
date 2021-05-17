package com.trendyol.devtools

import android.content.Context
import android.content.Intent
import com.trendyol.devtools.internal.service.DevToolsService
import com.trendyol.devtools.internal.ui.MainActivity

object TrendyolDevTools {

    fun init(context: Context) {
        DevToolsService.initializeService(context)
    }

    fun show(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}
