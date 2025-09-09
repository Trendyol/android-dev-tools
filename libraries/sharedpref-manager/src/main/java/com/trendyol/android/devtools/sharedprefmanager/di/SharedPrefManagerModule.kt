package com.trendyol.android.devtools.sharedprefmanager.di

import android.app.Application
import android.content.Context
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerRepository
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerRepositoryImpl
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerUseCase
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerUseCaseImpl
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefUpdateTypeValidator
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefManagerActivity
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefManagerViewModel
import embedded.koin.androidx.viewmodel.dsl.viewModelOf
import embedded.koin.core.module.Module
import embedded.koin.dsl.bind
import embedded.koin.dsl.module

internal fun sharedPrefManagerModule(
    application: Application,
): Module = module {
    single<Context> { application.applicationContext }
    single { SharedPrefUpdateTypeValidator() }

    scope<SharedPrefManagerActivity> {
        scoped { SharedPrefManagerRepositoryImpl(get()) } bind SharedPrefManagerRepository::class
        scoped { SharedPrefManagerUseCaseImpl(get()) } bind SharedPrefManagerUseCase::class
        viewModelOf(::SharedPrefManagerViewModel)
    }
}
