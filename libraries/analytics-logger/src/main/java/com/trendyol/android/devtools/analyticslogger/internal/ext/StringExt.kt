package com.trendyol.android.devtools.analyticslogger.internal.ext

import com.squareup.moshi.Moshi

internal fun String?.beautify(moshi: Moshi): String? {
    val adapter = moshi.adapter(Any::class.java)
    return runCatching {
        val jsonObject = adapter.fromJson(this.orEmpty())
        adapter.indent("    ").toJson(jsonObject)
    }.getOrElse {
        "Json parse error: $it"
    }
}
