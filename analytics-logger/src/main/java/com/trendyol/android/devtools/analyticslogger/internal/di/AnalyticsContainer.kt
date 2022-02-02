package com.trendyol.android.devtools.analyticslogger.internal.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.analyticslogger.internal.data.database.EventDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepositoryImpl
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManager
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManagerImpl

internal class AnalyticsContainer(private val context: Context) {

    private val moshi: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    private val eventDatabase: EventDatabase by lazy { EventDatabase.create(context) }

    private val eventRepository: EventRepository by lazy { EventRepositoryImpl(eventDatabase) }

    val eventManager: EventManager by lazy { EventManagerImpl(eventRepository, moshi) }
}
