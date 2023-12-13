package com.trendyol.android.devtools.debugmenu

import android.content.Context
import android.content.Intent
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
        ContextContainer.getContext().startActivity(newIntent(title))
    }

    /**
     * Creates an Intent to launch Debug menu.
     *
     * @param title to show above menu. Default is "Debug Menu".
     *
     * @return intent for Debug Menu's activity.
     */
    fun newIntent(title: String = "Debug Menu"): Intent =
        DebugMenuActivity.newIntent(ContextContainer.getContext(), title)

    fun addDebugAction(debugAction: DebugActionItem) {
        addDebugActionItems(listOf(debugAction))
    }

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        debugMenuUseCase.addDebugActionItems(debugActions)
    }
}
