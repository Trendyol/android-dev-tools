package com.trendyol.android.devtools.httpinspector.internal.domain.model.mock

import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData

internal data class MockData(
    val uid: Int = 0,
    val requestData: RequestData,
    val responseData: ResponseData,
    val isActive: Boolean = true,
)
