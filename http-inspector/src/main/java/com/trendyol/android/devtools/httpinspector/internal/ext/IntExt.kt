package com.trendyol.android.devtools.httpinspector.internal.ext

internal fun Int?.orZero(): Int {
    return this ?: 0
}
