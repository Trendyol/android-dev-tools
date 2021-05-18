package com.trendyol.devtools.internal.data

import android.content.SharedPreferences
import androidx.core.content.edit

internal class EnvironmentRepository(private val sharedPreferences: SharedPreferences) {

    fun getCurrentEnvironment(): String? = sharedPreferences.getString(KEY_ENVIRONMENT, null)

    fun updateCurrentEnvironment(environment: String) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_ENVIRONMENT, environment)
        }
    }

    companion object {

        private const val KEY_ENVIRONMENT = "environment"
    }
}
