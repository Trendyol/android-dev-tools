package com.trendyol.android.devtools.analyticslogger.internal.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.analyticslogger.api.platform.EventPlatform

@ProvidedTypeConverter
class PlatformConverter(private val moshi: Moshi) {

    @TypeConverter
    fun toPlatform(value: String?): EventPlatform? {
        val adapter = moshi.adapter(EventPlatform::class.java)
        return value?.let { adapter.fromJson(it) }
    }

    @TypeConverter
    fun toString(platform: EventPlatform?): String? {
        val adapter = moshi.adapter(EventPlatform::class.java)
        return adapter.toJson(platform)
    }
}
