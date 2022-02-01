package com.trendyol.android.devtools.httpinspector.internal.domain.model

data class RequestData(
    val url: String,
    val method: String,
    val headers: String,
    val body: String,
)
