package com.trendyol.devtools.environmentmanager.internal.di

import android.app.Application
import android.content.Context
import com.trendyol.devtools.environmentmanager.internal.data.EnvironmentRepository
import com.trendyol.devtools.environmentmanager.internal.domain.EnvironmentUseCase
import com.trendyol.devtools.environmentmanager.internal.ui.MainViewModel
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.androidx.viewmodel.dsl.viewModelOf
import embedded.koin.core.module.Module
import embedded.koin.dsl.module

internal fun environmentManagerModule(
    application: Application,
): Module = module {
    single<Context> { application.applicationContext }
    single {
        androidContext().getSharedPreferences("dev_tools_preferences", Context.MODE_PRIVATE)
    }

    single { EnvironmentRepository(get()) }
    single { EnvironmentUseCase(get()) }

    viewModelOf(::MainViewModel)
}
