package com.trendyol.android.devtools.httpinspector.internal.model

data class ResponseData(
    val code: Int,
    val headers: Map<String, List<String>>,
    val body: Any?,
)
