package com.trendyol.android.devtools.autofillservice.internal.coroutines

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope

interface CoroutineRunner {

    val job: CompletableJob

    val scope: CoroutineScope
}
