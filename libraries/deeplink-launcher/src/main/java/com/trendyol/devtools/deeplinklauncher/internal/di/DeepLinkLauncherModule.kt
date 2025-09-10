package com.trendyol.devtools.deeplinklauncher.internal.di

import android.app.Application
import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.devtools.deeplinklauncher.internal.data.database.DeepLinkDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.AppDeepLinkRepository
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.AppDeepLinkRepositoryImpl
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeepLinkHistoryRepository
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeepLinkHistoryRepositoryImpl
import com.trendyol.devtools.deeplinklauncher.internal.domain.AppDeepLinkUseCase
import com.trendyol.devtools.deeplinklauncher.internal.domain.AppDeepLinkUseCaseImpl
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkHistoryUseCase
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkHistoryUseCaseImpl
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkValidator
import com.trendyol.devtools.deeplinklauncher.internal.ui.MainViewModel
import com.trendyol.devtools.deeplinklauncher.internal.ui.list.DeepLinkListViewModel
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.androidx.viewmodel.dsl.viewModelOf
import embedded.koin.core.module.Module
import embedded.koin.dsl.bind
import embedded.koin.dsl.module

internal fun deepLinkLauncherModule(
    application: Application,
): Module = module {
    single<Context> { application.applicationContext }
    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
    single { DeepLinkDatabase.create(androidContext()) }

    single { AppDeepLinkRepositoryImpl(get()) } bind (AppDeepLinkRepository::class)
    single { DeepLinkHistoryRepositoryImpl(get()) } bind (DeepLinkHistoryRepository::class)

    single { AppDeepLinkUseCaseImpl(get()) } bind (AppDeepLinkUseCase::class)
    single { DeepLinkHistoryUseCaseImpl(get()) } bind (DeepLinkHistoryUseCase::class)

    single { DeepLinkValidator() }

    viewModelOf(::MainViewModel)
    viewModelOf(::DeepLinkListViewModel)
}
