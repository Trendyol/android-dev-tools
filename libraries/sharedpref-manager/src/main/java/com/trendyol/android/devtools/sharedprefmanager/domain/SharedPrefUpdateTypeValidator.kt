package com.trendyol.android.devtools.sharedprefmanager.domain

import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefValueType

class SharedPrefUpdateTypeValidator {

    fun validate(sharedPrefModel: SharedPrefModel, newValue: String) {
        when (sharedPrefModel.valueType) {
            SharedPrefValueType.INT -> {
                kotlin.runCatching {
                    newValue.toInt()
                }.onSuccess {

                }.onFailure {
                    throw IllegalArgumentException("Old value type is: ${sharedPrefModel.valueType} so new value type must be ${sharedPrefModel.valueType}")
                }
            }

            SharedPrefValueType.STRING -> {
                kotlin.runCatching {
                    newValue.toString()
                }.onSuccess {

                }.onFailure {
                    throw IllegalArgumentException("Old value type is: ${sharedPrefModel.valueType} so new value type must be ${sharedPrefModel.valueType}")
                }
            }

            SharedPrefValueType.BOOLEAN -> {
                kotlin.runCatching {
                    newValue.toBoolean()
                }.onSuccess {

                }.onFailure {
                    throw IllegalArgumentException("Old value type is: ${sharedPrefModel.valueType} so new value type must be ${sharedPrefModel.valueType}")
                }
            }

            SharedPrefValueType.FLOAT -> {
                kotlin.runCatching {
                    newValue.toFloat()
                }.onSuccess {

                }.onFailure {
                    throw IllegalArgumentException("Old value type is: ${sharedPrefModel.valueType} so new value type must be ${sharedPrefModel.valueType}")
                }
            }

            SharedPrefValueType.LONG -> {
                kotlin.runCatching {
                    newValue.toLong()
                }.onSuccess {

                }.onFailure {
                    throw IllegalArgumentException("Old value type is: ${sharedPrefModel.valueType} so new value type must be ${sharedPrefModel.valueType}")
                }
            }

            SharedPrefValueType.UNKNOWN -> {
                throw IllegalArgumentException("Unknown value type")
            }
        }
    }
}
