package com.trendyol.devtools.deeplinklauncher.internal.di

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal object ContextContainer {

    val mainContainer by lazy { MainContainer() }

    private lateinit var context: Context

    fun getContext(): Context =
        if (::context.isInitialized) {
            context
        } else {
            throw NullPointerException(
                "Library is not initialized, please call init(Application) on Application.onCreate()"
            )
        }

    fun setContext(context: Context) {
        this.context = context
    }
}
