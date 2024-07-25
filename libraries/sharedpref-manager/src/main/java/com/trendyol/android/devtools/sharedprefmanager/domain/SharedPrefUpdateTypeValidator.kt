package com.trendyol.android.devtools.sharedprefmanager.domain

import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefValueType

class SharedPrefUpdateTypeValidator {

    fun validate(sharedPrefModel: SharedPrefModel, newValue: String) {
        when (sharedPrefModel.valueType) {
            SharedPrefValueType.INT -> {
                kotlin.runCatching {
                    newValue.toInt()
                }.onFailure { throwExceptionFor(sharedPrefModel.valueType) }
            }

            SharedPrefValueType.STRING -> {
                kotlin.runCatching {
                    newValue.toString()
                }.onFailure { throwExceptionFor(sharedPrefModel.valueType) }
            }

            SharedPrefValueType.BOOLEAN -> {
                kotlin.runCatching {
                    newValue.toBoolean()
                }.onFailure { throwExceptionFor(sharedPrefModel.valueType) }
            }

            SharedPrefValueType.FLOAT -> {
                kotlin.runCatching {
                    newValue.toFloat()
                }.onFailure { throwExceptionFor(sharedPrefModel.valueType) }
            }

            SharedPrefValueType.LONG -> {
                kotlin.runCatching {
                    newValue.toLong()
                }.onSuccess {

                }.onFailure { throwExceptionFor(sharedPrefModel.valueType) }
            }

            SharedPrefValueType.UNKNOWN -> {
                throw IllegalArgumentException("Unknown value type")
            }
        }
    }

    private fun throwExceptionFor(valueType: SharedPrefValueType) {
        throw IllegalArgumentException("Old value type is: $valueType so new value type must be $valueType.")
    }
}
