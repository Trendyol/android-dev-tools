package com.trendyol.android.devtools.mock_interceptor.internal.ext

import okhttp3.RequestBody
import okio.Buffer

internal fun RequestBody?.readString(): String {
    val buffer = Buffer()
    this?.writeTo(buffer)
    return buffer.readUtf8()
}
