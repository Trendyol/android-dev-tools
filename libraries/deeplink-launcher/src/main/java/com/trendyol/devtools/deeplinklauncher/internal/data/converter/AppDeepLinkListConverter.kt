package com.trendyol.devtools.deeplinklauncher.internal.data.converter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList

class AppDeepLinkListConverter {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val adapter = moshi.adapter(DeepLinkList::class.java)

    @TypeConverter
    fun deepLinkListToString(value: DeepLinkList?): String? {
        return adapter.toJson(value)
    }

    @TypeConverter
    fun stringToDeepLinkList(deeplinks: String?): DeepLinkList? {
        return deeplinks?.let { adapter.fromJson(it) }
    }
}
