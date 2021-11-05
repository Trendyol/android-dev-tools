package com.trendyol.devtools.autofill.internal.ext

import com.trendyol.devtools.autofill.internal.coroutines.CoroutineRunner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal suspend fun <T> CoroutineRunner.main(
    block: suspend () -> T,
) = withContext(Dispatchers.Main) {
    block.invoke()
}

internal suspend fun <T> CoroutineRunner.default(
    block: suspend () -> T,
) = withContext(Dispatchers.Default) {
    block.invoke()
}

internal suspend fun <T> CoroutineRunner.io(
    block: suspend () -> T,
) = withContext(Dispatchers.IO) {
    block.invoke()
}

internal fun <T> CoroutineRunner.launchMain(
    block: suspend () -> T,
) = scope.launch(Dispatchers.Main) {
    block.invoke()
}

internal fun <T> CoroutineRunner.launchDefault(
    block: suspend () -> T,
) = scope.launch(Dispatchers.Default) {
    block.invoke()
}

internal fun <T> CoroutineRunner.launchIO(
    block: suspend () -> T,
) = scope.launch(Dispatchers.IO) {
    block.invoke()
}
