package com.trendyol.android.devtools.sharedprefmanager.domain

import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel

interface SharedPrefManagerRepository {

    suspend fun getAllSharedPref(): MutableMap<String, *>

    suspend fun updateStringValue(key: String, newValue: String)

    suspend fun updateIntValue(key: String, newValue: Int)

    suspend fun updateBooleanValue(key: String, newValue: Boolean)

    suspend fun updateLongValue(key: String, newValue: Long)

    suspend fun updateFloatValue(key: String, newValue: Float)

    suspend fun deleteSharedPrefItem(sharedPrefModel: SharedPrefModel)

    suspend fun deleteAllSharedPref()

}
