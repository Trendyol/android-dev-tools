package com.trendyol.android.devtools.sharedprefmanager.domain

import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel

interface SharedPrefManagerUseCase {

    suspend fun getAllSharedPref(): List<SharedPrefModel>

    suspend fun updateSharedPrefItem(sharedPrefModel: SharedPrefModel, newValue: String)

    suspend fun deleteSharedPrefItem(sharedPrefModel: SharedPrefModel)

    suspend fun deleteAllSharedPref()

}
