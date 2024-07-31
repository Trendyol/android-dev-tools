package com.trendyol.android.devtools.sharedprefmanager.data

import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefValueTypeConverter.getValueType

data class SharedPrefModel(
    val key: String,
    val value: Any,
    val valueType: SharedPrefValueType = getValueType(value)
)
