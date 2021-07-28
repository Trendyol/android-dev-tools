package com.trendyol.devtools.httpdebug.internal.di

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal object ContextContainer {

    private lateinit var context: Context

    fun getContext(): Context =
        if (::context.isInitialized) {
            context
        } else {
            throw NullPointerException(
                "Dev Tools library is not initialized, please call TrendyolDevTools.init(Application) on " +
                    "Application.onCreate to use Trendyol Dev Tools."
            )
        }

    fun setContext(context: Context) {
        this.context = context
    }
}
