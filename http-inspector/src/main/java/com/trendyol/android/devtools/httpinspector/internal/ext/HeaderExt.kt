package com.trendyol.android.devtools.httpinspector.internal.ext

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Headers

internal fun Map<String, List<String>>?.toHeaders(): Headers {
    return Headers.Builder().apply {
        this@toHeaders?.forEach { pair ->
            pair.value.forEach { value ->
                add(pair.key, value)
            }
        }
    }.build()
}

internal fun String?.toHeaderMap(moshi: Moshi): Map<String, List<String>> {
    val listType = Types.newParameterizedType(
        List::class.java,
        String::class.java
    )
    val mapType = Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        listType,
    )
    val adapter: JsonAdapter<Map<String, List<String>>> = moshi.adapter(mapType)
    return runCatching { requireNotNull(adapter.fromJson(this@toHeaderMap.orEmpty())) }
        .getOrDefault(emptyMap())
}

internal fun Headers?.toJson(moshi: Moshi): String {
    val listType = Types.newParameterizedType(
        List::class.java,
        String::class.java,
    )
    val mapType = Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        listType,
    )
    val adapter: JsonAdapter<Map<String, List<String>>> = moshi.adapter(mapType)
    return runCatching { adapter.toJson(this@toJson?.toMultimap()) }
        .getOrDefault("")
}
