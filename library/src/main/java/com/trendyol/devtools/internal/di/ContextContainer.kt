package com.trendyol.devtools.internal.di

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal object ContextContainer {

    private lateinit var context: Context
    private lateinit var environments: List<String>

    fun getContext(): Context =
        if (::context.isInitialized) context else throw NullPointerException("context is not initialized, please call TrendyolDevTools.init(Application) on Application.onCreate to use Trendyol Dev Tools.")

    fun setContext(context: Context) {
        this.context = context
    }

    fun setEnvironments(environments: List<String>) {
        this.environments = environments
    }

    fun getEnvironmentsContainer(): EnvironmentContainer =
        EnvironmentContainer(context, environments)

    fun getMainContainer(): MainContainer = MainContainer(getEnvironmentsContainer())
}
