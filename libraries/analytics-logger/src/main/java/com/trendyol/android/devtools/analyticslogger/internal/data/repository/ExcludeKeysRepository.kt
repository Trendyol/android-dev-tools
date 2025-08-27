package com.trendyol.android.devtools.analyticslogger.internal.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit

internal class ExcludeKeysRepository(
    private val sharedPreferences: SharedPreferences
) {

    fun saveExcludedKeys(keys: String) {
        sharedPreferences.edit {
            putString(EXCLUDED_KEYS_KEY, keys)
        }
    }

    fun getExcludedKeys(): String {
        return sharedPreferences.getString(EXCLUDED_KEYS_KEY, "").orEmpty()
    }

    companion object {
        private const val EXCLUDED_KEYS_KEY = "excluded_event_keys"
    }
}
