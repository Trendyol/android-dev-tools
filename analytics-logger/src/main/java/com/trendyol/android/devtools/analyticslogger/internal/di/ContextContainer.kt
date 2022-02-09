package com.trendyol.android.devtools.analyticslogger.internal.di

import android.app.Application
import android.content.Context

internal object ContextContainer {

    val analyticsContainer by lazy { AnalyticsContainer(application.applicationContext) }

    val mainContainer by lazy { MainContainer(analyticsContainer) }

    private lateinit var application: Application

    fun getContext(): Context =
        if (::application.isInitialized) {
            application.applicationContext
        } else {
            throw NullPointerException("Analytics Logger library is not initialized.")
        }

    fun initialize(application: Application) {
        this.application = application
    }
}
