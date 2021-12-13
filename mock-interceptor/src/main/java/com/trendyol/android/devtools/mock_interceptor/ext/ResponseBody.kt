package com.trendyol.android.devtools.mock_interceptor.ext

import okhttp3.ResponseBody

internal fun ResponseBody?.readString(): String {
    return this?.string().orEmpty()
}
