package com.trendyol.android.devtools.debugmenu

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
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

    fun setDebugActionItems(debugActionItems: List<DebugActionItem>) {
        debugMenuUseCase.setDebugActionItems(debugActionItems)
    }

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        debugMenuUseCase.addDebugActionItems(debugActions)
    }

    fun addDebugAction(debugAction: DebugActionItem) {
        debugMenuUseCase.addDebugAction(debugAction)
    }

    fun getDebugActionClickEvent(): LiveData<DebugActionItem> = debugMenuUseCase.getDebugActionClickEvent()
}
