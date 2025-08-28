package com.trendyol.android.devtools.sharedprefmanager.di

import android.content.Context
import android.content.SharedPreferences
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerRepository
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerRepositoryImpl
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerUseCase
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerUseCaseImpl

internal class SharedPrefUseCaseContainer(
    private val context: Context,
    private val provider: SharedPreferencesProvider,
) {

    private val sharedPref: SharedPreferences by lazy {
        provider.provide(context)
    }

    private val sharedPrefManagerRepository: SharedPrefManagerRepository by lazy {
        SharedPrefManagerRepositoryImpl(sharedPref)
    }

    val sharedPrefManagerUseCase: SharedPrefManagerUseCase by lazy {
        SharedPrefManagerUseCaseImpl(sharedPrefManagerRepository)
    }
}
