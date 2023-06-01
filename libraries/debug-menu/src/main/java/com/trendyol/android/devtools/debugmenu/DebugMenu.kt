package com.trendyol.android.devtools.debugmenu

import android.app.Application
import android.content.Intent
import com.trendyol.android.devtools.debugmenu.internal.di.ContextContainer
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase
import com.trendyol.android.devtools.debugmenu.internal.ui.DebugMenuActivity

object DebugMenu {

    private lateinit var debugMenuUseCase: DebugMenuUseCase

    fun init(application: Application) {
        ContextContainer.setContext(application)
        debugMenuUseCase = ContextContainer.debugMenuContainer.debugMenuUseCase
    }

    fun show() {
        val context = ContextContainer.getContext()
        context.startActivity(Intent(context, DebugMenuActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun addDebugAction(debugAction: DebugActionItem) {
        addDebugActionItems(listOf(debugAction))
    }

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        debugMenuUseCase.addDebugActionItems(debugActions)
    }
}
