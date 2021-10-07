package com.trendyol.devtools.autofill.internal.ext

fun Any.sameInstanceWith(other: Any): Boolean {
    return this::class.java == other::class.java
}
