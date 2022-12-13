package com.trendyol.devtools.deeplinklauncher.internal.di

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal object ContextContainer {

    private val deeplinkContainer by lazy { DeepLinkContainer(context) }

    val mainContainer by lazy { MainContainer(deeplinkContainer) }
    val deepLinkListContainer by lazy { DeepLinkListContainer(deeplinkContainer) }

    val appDeepLinkUseCase by lazy { deeplinkContainer.appDeepLinkUseCase }

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
