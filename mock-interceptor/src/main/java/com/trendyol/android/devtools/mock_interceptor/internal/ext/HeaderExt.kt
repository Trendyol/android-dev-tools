package com.trendyol.android.devtools.mock_interceptor.internal.ext

import okhttp3.Headers

fun Map<String, List<String>>.toHeaders(): Headers {
    return Headers.Builder().apply {
        this@toHeaders.forEach { pair ->
            pair.value.forEach { value ->
                add(pair.key, value)
            }
        }
    }.build()
}
