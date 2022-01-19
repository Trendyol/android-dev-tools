package com.trendyol.android.devtools.httpinspector.internal.domain.usecase

import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMockDataUseCase(
    private val mockRepository: MockRepository,
) {

    operator fun invoke(): Flow<List<MockData>> {
        return mockRepository.getAll().map {
            it.map { entity ->
                MockData(
                    requestData = RequestData(
                        url = entity.url.orEmpty(),
                        method = "sa",
                        headers = mapOf(),
                        body = entity.requestBody.orEmpty(),
                    ),
                    responseData = ResponseData(
                        code = 200,
                        headers = mapOf(),
                        body = entity.responseBody.orEmpty(),
                    )
                )
            }
        }
    }
}
