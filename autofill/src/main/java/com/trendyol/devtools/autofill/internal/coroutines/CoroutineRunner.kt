package com.trendyol.devtools.autofill.internal.coroutines

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope

internal interface CoroutineRunner {

    val job: CompletableJob

    val scope: CoroutineScope
}
