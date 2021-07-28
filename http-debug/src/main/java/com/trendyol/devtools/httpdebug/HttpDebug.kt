package com.trendyol.devtools.httpdebug

import android.app.Application
import android.content.Intent
import com.trendyol.devtools.httpdebug.internal.di.ContextContainer
import com.trendyol.devtools.httpdebug.internal.di.MainContainer
import com.trendyol.devtools.httpdebug.internal.ui.HttpDebugActivity

object HttpDebug {

    private val manipulatorUseCase = MainContainer.manipulatorUseCase

    fun init(application: Application) {
        ContextContainer.setContext(application)
    }

    fun show() {
        val context = ContextContainer.getContext()
        val intent = Intent(context, HttpDebugActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun enable() {
        manipulatorUseCase.setToggleToTrue()
    }

    fun disable() {
        manipulatorUseCase.setToggleToFalse()
    }
}
