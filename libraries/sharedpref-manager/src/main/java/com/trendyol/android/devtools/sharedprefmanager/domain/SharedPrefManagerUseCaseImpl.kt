package com.trendyol.android.devtools.sharedprefmanager.domain

import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefValueType

class SharedPrefManagerUseCaseImpl(
    private val sharedPrefManagerRepository: SharedPrefManagerRepository,
) : SharedPrefManagerUseCase {

    override suspend fun getAllSharedPref(): List<SharedPrefModel> {

        return sharedPrefManagerRepository.getAllSharedPref().mapNotNull {
            it.value?.let { it1 -> SharedPrefModel(it.key, it1) }
        }.sortedBy { it.key }

    }

    override suspend fun updateSharedPrefItem(sharedPrefModel: SharedPrefModel, newValue: String) {
        when (sharedPrefModel.valueType) {
            SharedPrefValueType.STRING -> {
                sharedPrefManagerRepository.updateStringValue(sharedPrefModel.key, newValue)
            }
            SharedPrefValueType.INT -> {
                sharedPrefManagerRepository.updateIntValue(sharedPrefModel.key, newValue.toInt())
            }
            SharedPrefValueType.BOOLEAN -> {
                sharedPrefManagerRepository.updateBooleanValue(sharedPrefModel.key, newValue.toBoolean())
            }
            SharedPrefValueType.LONG -> {
                sharedPrefManagerRepository.updateLongValue(sharedPrefModel.key, newValue.toLong())
            }
            SharedPrefValueType.FLOAT -> {
                sharedPrefManagerRepository.updateFloatValue(sharedPrefModel.key, newValue.toFloat())
            }
            SharedPrefValueType.UNKNOWN -> {}
        }
    }

    override suspend fun deleteSharedPrefItem(sharedPrefModel: SharedPrefModel) {
        sharedPrefManagerRepository.deleteSharedPrefItem(sharedPrefModel)
    }

    override suspend fun deleteAllSharedPref() {
        sharedPrefManagerRepository.deleteAllSharedPref()
    }
}
