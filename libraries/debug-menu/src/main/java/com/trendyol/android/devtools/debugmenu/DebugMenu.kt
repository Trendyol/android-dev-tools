package com.trendyol.android.devtools.debugmenu

import android.content.Context
import android.content.Intent
import com.trendyol.android.devtools.debugmenu.internal.di.debugMenuModule
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase
import com.trendyol.android.devtools.debugmenu.internal.ui.DebugMenuActivity
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.android.ext.koin.androidLogger
import embedded.koin.core.Koin
import embedded.koin.dsl.koinApplication

object DebugMenu {

    internal lateinit var koin: Koin

    private val context: Context by lazy { koin.get() }
    private val debugMenuUseCase: DebugMenuUseCase by lazy { koin.get() }

    /**
     * Initializes library, should be called earlier than [show].
     *
     * @param context application context.
     */
    fun init(context: Context) {
        koin = koinApplication {
            androidContext(context)
            androidLogger()

            modules(
                debugMenuModule(
                    context = context,
                )
            )
        }.koin
    }

    /**
     * Starts [DebugMenuActivity].
     *
     * @param title to show above menu. Default is "Debug Menu".
     */
    fun show(title: String = "Debug Menu") {
        context.startActivity(newIntent(title))
    }

    /**
     * Creates an Intent to launch Debug menu.
     *
     * @param title to show above menu. Default is "Debug Menu".
     *
     * @return intent for Debug Menu's activity.
     */
    fun newIntent(title: String = "Debug Menu"): Intent =
        DebugMenuActivity.newIntent(context, title)

    fun addDebugAction(debugAction: DebugActionItem) {
        addDebugActionItems(listOf(debugAction))
    }

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        debugMenuUseCase.addDebugActionItems(debugActions)
    }
}
