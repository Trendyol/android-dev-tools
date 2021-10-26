package com.trendyol.devtools.autofill.internal.coroutines

import kotlinx.coroutines.CoroutineDispatcher

internal interface DispatcherProvider {

    fun main(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}
