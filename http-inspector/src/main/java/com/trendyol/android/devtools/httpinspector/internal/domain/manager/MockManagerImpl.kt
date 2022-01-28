package com.trendyol.android.devtools.httpinspector.internal.domain.manager

import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.httpinspector.internal.WebServer
import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData

class MockManagerImpl(
    private val mockRepository: MockRepository,
    private val moshi: Moshi,
) : MockManager {

    override suspend fun getAll(): Result<String> {
        val adapter = moshi.adapter(WebServer.Wrapper::class.java)
        return runCatching {
            val data = mockRepository.getAll().map { entity ->
                MockData(
                    uid = entity.uid,
                    isActive = entity.isActive,
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
            adapter.toJson(WebServer.Wrapper(data))
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
        mockRepository.delete(uid)
    }

    override suspend fun setActive(uid: Int, isActive: Boolean) {
        mockRepository.setActive(uid, isActive)
    }
}
