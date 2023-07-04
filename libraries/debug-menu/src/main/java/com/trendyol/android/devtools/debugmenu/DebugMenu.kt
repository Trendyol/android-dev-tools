package com.trendyol.android.devtools.debugmenu

import android.content.Context
import com.trendyol.android.devtools.debugmenu.internal.di.ContextContainer
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase
import com.trendyol.android.devtools.debugmenu.internal.ui.DebugMenuActivity

object DebugMenu {

    private lateinit var debugMenuUseCase: DebugMenuUseCase

    /**
     * Initializes library, should be called earlier than [show].
     *
     * @param context application context.
     */
    fun init(context: Context) {
        ContextContainer.setContext(context)
        debugMenuUseCase = ContextContainer.debugMenuContainer.debugMenuUseCase
    }

    /**
     * Starts [DebugMenuActivity].
     *
     * @param title to show above menu. Default is "Debug Menu".
     */
    fun show(title: String = "Debug Menu") {
        val context = ContextContainer.getContext()
        context.startActivity(DebugMenuActivity.newIntent(context, title))
    }

    fun addDebugAction(debugAction: DebugActionItem) {
        addDebugActionItems(listOf(debugAction))
    }

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        debugMenuUseCase.addDebugActionItems(debugActions)
    }
}
