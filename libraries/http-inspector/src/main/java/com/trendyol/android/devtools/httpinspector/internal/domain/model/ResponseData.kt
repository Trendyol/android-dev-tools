package com.trendyol.android.devtools.httpinspector.internal.domain.model

internal data class ResponseData(
    val code: Int,
    val headers: String?,
    val body: String?,
)
