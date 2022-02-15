package com.trendyol.android.devtools.httpinspector.internal.ext

import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import com.trendyol.android.devtools.httpinspector.internal.domain.model.mock.MockData

internal fun MockData.mapToMockEntity(): MockEntity {
    return MockEntity(
        url = requestData.url,
        method = requestData.method,
        requestHeaders = requestData.headers,
        requestBody = requestData.body,
        responseHeaders = responseData.headers,
        responseBody = responseData.body.toString(),
        code = responseData.code,
    )
}
