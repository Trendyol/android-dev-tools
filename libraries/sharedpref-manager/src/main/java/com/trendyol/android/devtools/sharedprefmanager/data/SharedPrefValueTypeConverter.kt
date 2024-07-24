package com.trendyol.android.devtools.sharedprefmanager.data

object SharedPrefValueTypeConverter {

    fun getValueType(value: Any): SharedPrefValueType {
        return when(value) {
            is String -> SharedPrefValueType.STRING
            is Int -> SharedPrefValueType.INT
            is Boolean -> SharedPrefValueType.BOOLEAN
            is Float -> SharedPrefValueType.FLOAT
            is Long -> SharedPrefValueType.LONG
            else -> SharedPrefValueType.UNKNOWN
        }
    }
}
