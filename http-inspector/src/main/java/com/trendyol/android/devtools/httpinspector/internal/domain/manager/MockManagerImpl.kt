package com.trendyol.android.devtools.httpinspector.internal.domain.manager

import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData

class MockManagerImpl(
    private val mockRepository: MockRepository,
) : MockManager {

    override suspend fun getAll(): List<MockData> {
        return mockRepository.getAll().map { entity ->
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

    override suspend fun insert(mockData: MockData) {
        mockRepository.insert(
            MockEntity(
                url = mockData.requestData.url,
                method = mockData.requestData.method,
                requestBody = mockData.requestData.body,
                responseBody = mockData.responseData.body.toString(),
            )
        )
    }

    override suspend fun delete(uid: Int) {
        TODO("Not yet implemented")
    }
}
