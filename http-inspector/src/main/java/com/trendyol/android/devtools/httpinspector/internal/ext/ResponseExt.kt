package com.trendyol.android.devtools.httpinspector.internal.ext

import okhttp3.ResponseBody

fun ResponseBody?.readString(): String {
    return this?.string().orEmpty()
}
