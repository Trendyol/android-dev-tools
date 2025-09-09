package com.trendyol.android.devtools.analyticslogger.internal.di

import android.app.Application
import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.analyticslogger.internal.NotificationManager
import com.trendyol.android.devtools.analyticslogger.internal.data.database.EventDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepositoryImpl
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.ExcludeKeysRepository
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManager
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManagerImpl
import com.trendyol.android.devtools.analyticslogger.internal.domain.usecase.ExcludeKeysUseCase
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.androidx.viewmodel.dsl.viewModelOf
import embedded.koin.core.module.Module
import embedded.koin.dsl.bind
import embedded.koin.dsl.module

internal fun analyticsLoggerModule(
    application: Application,
    showNotification: Boolean,
): Module = module {
    single<Context> { application.applicationContext }
    single { NotificationManager(showNotification) }
    single {
        androidContext().getSharedPreferences("analytics_logger", Context.MODE_PRIVATE)
    }
    single { EventDatabase.create(get()) }
    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    single { EventRepositoryImpl(get()) } bind(EventRepository::class)
    single { EventManagerImpl(get(), get()) } bind(EventManager::class)

    single { ExcludeKeysRepository(get()) }
    single { ExcludeKeysUseCase(get()) }

    viewModelOf(::MainViewModel)
}