package com.trendyol.devtools.environmentmanager.internal.di

import android.content.Context
import android.content.SharedPreferences
import com.trendyol.devtools.environmentmanager.internal.data.EnvironmentRepository
import com.trendyol.devtools.environmentmanager.internal.domain.EnvironmentUseCase

internal class EnvironmentContainer(private val context: Context) {

    val environmentUseCase by lazy { EnvironmentUseCase(repository = provideEnvironmentRepository()) }

    private fun provideEnvironmentRepository(): EnvironmentRepository =
        EnvironmentRepository(sharedPreferences = provideSharedPreferences())

    private fun provideSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    companion object {

        private const val SHARED_PREFERENCES_NAME = "dev_tools_preferences"
    }
}
