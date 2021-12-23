package com.trendyol.android.devtools.debugmenu.internal.di

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal object ContextContainer {

    val debugMenuContainer by lazy { DebugMenuContainer() }

    private lateinit var context: Context

    fun getContext(): Context =
        if (ContextContainer::context.isInitialized) {
            context
        } else {
            throw NullPointerException(
                "Dev Tools library is not initialized, please call TrendyolDevTools.init(Application) on " +
                    "Application.onCreate to use Trendyol Dev Tools."
            )
        }

    fun setContext(context: Context) {
        ContextContainer.context = context
    }
}
