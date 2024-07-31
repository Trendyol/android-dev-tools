package com.trendyol.android.devtools.sharedprefmanager.di

import android.content.Context
import android.content.SharedPreferences
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerRepository
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerRepositoryImpl
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerUseCase
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerUseCaseImpl

internal class SharedPrefUseCaseContainer(
    private val context: Context,
    private val sharedPrefName: String
) {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
    }

    private val sharedPrefManagerRepository: SharedPrefManagerRepository by lazy {
        SharedPrefManagerRepositoryImpl(sharedPref)
    }

    val sharedPrefManagerUseCase: SharedPrefManagerUseCase by lazy {
        SharedPrefManagerUseCaseImpl(sharedPrefManagerRepository)
    }
}
