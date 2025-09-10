package com.trendyol.android.devtools.analyticslogger.internal.di

import com.trendyol.android.devtools.analyticslogger.AnalyticsLogger
import embedded.koin.core.Koin
import embedded.koin.core.component.KoinComponent

internal interface AnalyticsLoggerKoinComponent : KoinComponent {

    override fun getKoin(): Koin {
        return AnalyticsLogger.koin
    }
}
