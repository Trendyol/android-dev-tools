package com.trendyol.devtools.autofill.internal.io

import android.content.Context

internal object FileReader {

    fun readAssetFile(context: Context, name: String): String? {
        return runCatching {
            context.assets.open(name)
                .bufferedReader()
                .use { it.readText() }
        }.getOrNull()
    }
}
