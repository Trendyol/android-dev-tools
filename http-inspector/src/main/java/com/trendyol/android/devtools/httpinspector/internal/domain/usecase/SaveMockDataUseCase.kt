package com.trendyol.android.devtools.httpinspector.internal.domain.usecase

import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaveMockDataUseCase(
    private val mockRepository: MockRepository,
) {

    suspend operator fun invoke(mockData: MockData) {
        mockRepository.insert(
            MockEntity(
                url = mockData.requestData.url,
                method = mockData.requestData.method,
                requestBody = mockData.requestData.body,
                responseBody = mockData.responseData.body.toString(),
            )
        )
    }
}
