package com.trendyol.android.devtools.mock_interceptor.internal.ext

import okhttp3.ResponseBody

fun ResponseBody?.readString(): String {
    return this?.string().orEmpty()
}
