package com.trendyol.devtools.environmentmanager.internal.data

import android.content.SharedPreferences
import androidx.core.content.edit

internal class EnvironmentRepository(private val sharedPreferences: SharedPreferences) {

    fun getEnvironments(): List<String>? = sharedPreferences.getStringSet(KEY_ENVIRONMENTS, null)?.toList()

    fun updateEnvironments(environments: List<String>) {
        sharedPreferences.edit(commit = true) {
            putStringSet(KEY_ENVIRONMENTS, environments.toSet())
        }
    }

    fun getCurrentEnvironment(): String? = sharedPreferences.getString(KEY_ENVIRONMENT, null)

    fun updateCurrentEnvironment(environment: String) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_ENVIRONMENT, environment)
        }
    }

    companion object {

        private const val KEY_ENVIRONMENTS = "environments"
        private const val KEY_ENVIRONMENT = "environment"
    }
}
