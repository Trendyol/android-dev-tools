package com.trendyol.android.devtools.httpinspector.internal.domain.model

data class ResponseData(
    val code: Int,
    val headers: Map<String, List<String>>,
    val body: Any?,
)
