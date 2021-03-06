package com.trendyol.android.devtools.httpinspector.internal.ext

import com.squareup.moshi.JsonAdapter

internal fun <T> JsonAdapter<T>.safeParse(json: String?): T? {
    return runCatching { fromJson(json.orEmpty()) }.getOrNull()
}
