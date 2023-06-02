package com.trendyol.android.devtools.analyticslogger

import android.app.Application
import android.util.Log
import com.trendyol.android.devtools.analyticslogger.internal.NotificationManager
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer

object AnalyticsLogger {

    internal var instance: NotificationManager? = null
    private const val TAG = "AnalyticsLogger"
    private const val INIT_ERROR_MESSAGE = "Should call AnalyticsLogger.init(Application, Boolean) first."

    fun init(application: Application, showNotification: Boolean = true) {
        ContextContainer.initialize(application)
        instance = NotificationManager(showNotification)
    }

    fun show() {
        instance?.show() ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }

    fun showNotification() {
        instance?.showNotification() ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }

    fun hideNotification() {
        instance?.hideNotification() ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }

    fun report(key: String?, value: String?, platform: String?) {
        instance?.reportEvent(key, value, platform) ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }
}
