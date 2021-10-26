package com.trendyol.devtools.autofill.internal.io

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal object FileReader {

    suspend fun readAssetFile(context: Context, name: String) = withContext(Dispatchers.IO) {
        runCatching {
            context.assets.open(name)
                .bufferedReader()
                .use{ it.readText() }
        }.getOrNull()
    }
}
