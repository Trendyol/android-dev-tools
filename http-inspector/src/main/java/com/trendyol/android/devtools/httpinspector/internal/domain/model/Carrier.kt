package com.trendyol.android.devtools.httpinspector.internal.domain.model

internal data class Carrier(
    val id: Int,
    val requestData: RequestData,
    val responseData: ResponseData,
    val requestTimeInMillis: Long,
    val responseTimeInMillis: Long,
    val cURL: String
)
