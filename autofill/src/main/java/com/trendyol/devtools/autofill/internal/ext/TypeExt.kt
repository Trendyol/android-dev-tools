package com.trendyol.devtools.autofill.internal.ext

internal fun Any.sameInstanceWith(other: Any): Boolean {
    return this::class.java == other::class.java
}
