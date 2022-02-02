package com.trendyol.android.devtools.analyticslogger

import android.app.Application

class AnalyticsLogger private constructor(
    private val application: Application,
) {

    companion object {

        fun init(application: Application) {
            // no-op
        }

        fun report(
            key: String?,
            value: Any?,
            platform: String?,
        ) {
            // no-op
        }
    }
}
