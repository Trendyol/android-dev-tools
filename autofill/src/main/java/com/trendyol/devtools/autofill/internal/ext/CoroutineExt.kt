package com.trendyol.devtools.autofill.internal.ext

import com.trendyol.devtools.autofill.internal.coroutines.CoroutineRunner
import com.trendyol.devtools.autofill.internal.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal suspend fun <T> CoroutineRunner.main(
    block: suspend () -> T,
) = withContext(dispatcherProvider.main()) {
    block.invoke()
}

internal suspend fun <T> CoroutineRunner.default(
    block: suspend () -> T,
) = withContext(dispatcherProvider.default()) {
    block.invoke()
}

internal suspend fun <T> CoroutineRunner.io(
    block: suspend () -> T,
) = withContext(dispatcherProvider.io()) {
    block.invoke()
}

internal fun <T> CoroutineRunner.launchMain(
    block: suspend () -> T,
) = scope.launch(dispatcherProvider.main()) {
    block.invoke()
}

internal fun <T> CoroutineRunner.launchDefault(
    block: suspend () -> T,
) = scope.launch(dispatcherProvider.default()) {
    block.invoke()
}

internal fun <T> CoroutineRunner.launchIO(
    block: suspend () -> T,
) = scope.launch(dispatcherProvider.io()) {
    block.invoke()
}
