package com.trendyol.android.devtools.httpinspector.internal.domain.model

data class Carrier(
    val id: Int,
    val requestData: RequestData,
    val responseData: ResponseData,
)