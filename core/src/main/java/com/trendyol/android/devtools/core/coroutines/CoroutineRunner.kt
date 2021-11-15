package com.trendyol.android.devtools.core.coroutines

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope

interface CoroutineRunner {

    val job: CompletableJob

    val scope: CoroutineScope
}
