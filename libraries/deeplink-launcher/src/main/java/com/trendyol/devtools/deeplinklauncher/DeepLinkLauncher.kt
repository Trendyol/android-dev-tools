package com.trendyol.devtools.deeplinklauncher

import android.app.Application
import android.content.Intent
import com.trendyol.devtools.deeplinklauncher.internal.di.ContextContainer
import com.trendyol.devtools.deeplinklauncher.internal.ui.MainActivity

object DeepLinkLauncher {

    fun init(application: Application) {
        ContextContainer.setContext(application)
    }

    fun show() {
        val context = ContextContainer.getContext()
        context.startActivity(
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
