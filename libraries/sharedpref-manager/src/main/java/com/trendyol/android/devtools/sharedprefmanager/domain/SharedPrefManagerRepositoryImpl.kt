package com.trendyol.android.devtools.sharedprefmanager.domain

import android.content.SharedPreferences
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel

class SharedPrefManagerRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPrefManagerRepository {

    override suspend fun getAllSharedPref(): MutableMap<String, *> {
        return sharedPreferences.all
    }

    override suspend fun updateStringValue(key: String, newValue: String) {
        sharedPreferences.edit().putString(key, newValue).apply()
    }

    override suspend fun updateIntValue(key: String, newValue: Int) {
        sharedPreferences.edit().putInt(key, newValue).apply()
    }

    override suspend fun updateBooleanValue(key: String, newValue: Boolean) {
        sharedPreferences.edit().putBoolean(key, newValue).apply()
    }

    override suspend fun updateLongValue(key: String, newValue: Long) {
        sharedPreferences.edit().putLong(key, newValue).apply()
    }

    override suspend fun updateFloatValue(key: String, newValue: Float) {
        sharedPreferences.edit().putFloat(key, newValue).apply()
    }

    override suspend fun deleteSharedPrefItem(sharedPrefModel: SharedPrefModel) {
        sharedPreferences.edit().remove(sharedPrefModel.key).apply()
    }

    override suspend fun deleteAllSharedPref() {
        sharedPreferences.edit().clear().apply()
    }
}
