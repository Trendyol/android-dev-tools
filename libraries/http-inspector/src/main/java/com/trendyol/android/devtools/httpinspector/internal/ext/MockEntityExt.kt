package com.trendyol.android.devtools.httpinspector.internal.ext

import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import com.trendyol.android.devtools.httpinspector.internal.domain.model.mock.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData

internal fun MockEntity.mapToMockData(): MockData {
    return MockData(
        uid = uid,
        isActive = isActive,
        requestData = RequestData(
            url = url.orEmpty(),
            method = method.orEmpty(),
            headers = requestHeaders.orEmpty(),
            body = requestBody.orEmpty(),
        ),
        responseData = ResponseData(
            code = code.orZero(),
            headers = responseHeaders.orEmpty(),
            body = responseBody.orEmpty(),
        )
    )
}
