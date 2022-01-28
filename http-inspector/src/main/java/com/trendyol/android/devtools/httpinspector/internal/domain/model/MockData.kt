package com.trendyol.android.devtools.httpinspector.internal.domain.model

data class MockData(
    val uid: Int = 0,
    val requestData: RequestData,
    val responseData: ResponseData,
    val isActive: Boolean = true,
)
