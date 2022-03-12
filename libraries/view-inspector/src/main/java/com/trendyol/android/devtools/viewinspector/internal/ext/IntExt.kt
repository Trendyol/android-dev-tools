package com.trendyol.android.devtools.viewinspector.internal.ext

internal fun Int?.orZero(): Int {
    return this ?: 0
}
