package com.trendyol.android.devtools.analyticslogger

import android.app.Application

@Suppress("unused", "UNUSED_PARAMETER")
object AnalyticsLogger {

    fun init(application: Application, showNotification: Boolean = true) {
        // no-op
    }

    fun setEventTransformFunction(jsFunction: String) {
    }

    fun getEventTransformFunction(): String {
        return ""
    }

    fun show() {
    }

    fun showNotification() {
    }

    fun hideNotification() {
    }

    fun report(key: String?, value: String?, platform: String?, source: String?) {
    }

    fun report(
        key: String?,
        value: String?,
        platform: String?,
        source: String?,
        isSuccess: Boolean? = null,
    ) {
    }
}
