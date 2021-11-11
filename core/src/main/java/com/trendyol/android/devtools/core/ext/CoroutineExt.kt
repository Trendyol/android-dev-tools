package com.trendyol.android.devtools.core.ext

import com.trendyol.android.devtools.core.coroutines.CoroutineRunner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun <T> CoroutineRunner.main(
    block: suspend () -> T,
) = withContext(Dispatchers.Main) {
    block.invoke()
}

suspend fun <T> CoroutineRunner.default(
    block: suspend () -> T,
) = withContext(Dispatchers.Default) {
    block.invoke()
}

suspend fun <T> CoroutineRunner.io(
    block: suspend () -> T,
) = withContext(Dispatchers.IO) {
    block.invoke()
}

fun <T> CoroutineRunner.launchMain(
    block: suspend () -> T,
) = scope.launch(Dispatchers.Main) {
    block.invoke()
}

fun <T> CoroutineRunner.launchDefault(
    block: suspend () -> T,
) = scope.launch(Dispatchers.Default) {
    block.invoke()
}

fun <T> CoroutineRunner.launchIO(
    block: suspend () -> T,
) = scope.launch(Dispatchers.IO) {
    block.invoke()
}
