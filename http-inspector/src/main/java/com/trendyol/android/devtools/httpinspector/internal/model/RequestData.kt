package com.trendyol.android.devtools.httpinspector.internal.model

data class RequestData(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>,
    val body: String,
)
