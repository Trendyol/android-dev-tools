package com.trendyol.android.devtools.httpinspector.internal.ext

fun Int?.orZero(): Int {
    return this ?: 0
}
