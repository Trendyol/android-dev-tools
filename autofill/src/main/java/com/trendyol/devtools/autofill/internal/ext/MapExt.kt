package com.trendyol.devtools.autofill.internal.ext

internal fun <K, V> Map<K, V>.foreachMap(block: (key: K, value: V) -> Unit) {
    forEach { block.invoke(it.key, it.value) }
}
