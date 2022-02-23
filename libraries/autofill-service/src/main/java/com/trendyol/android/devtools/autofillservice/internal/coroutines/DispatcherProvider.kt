package com.trendyol.android.devtools.autofillservice.internal.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun main(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}
