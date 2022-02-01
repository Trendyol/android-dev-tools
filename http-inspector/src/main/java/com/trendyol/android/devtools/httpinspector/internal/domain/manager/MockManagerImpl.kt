package com.trendyol.android.devtools.httpinspector.internal.domain.manager

import android.util.Log
import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.httpinspector.internal.WebServer
import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import com.trendyol.android.devtools.httpinspector.internal.ext.orZero

class MockManagerImpl(
    private val mockRepository: MockRepository,
    private val moshi: Moshi,
) : MockManager {

    override suspend fun getAll(): List<MockData> {
        return mockRepository.getAll().map { entity ->
            MockData(
                uid = entity.uid,
                isActive = entity.isActive,
                requestData = RequestData(
                    url = entity.url.orEmpty(),
                    method = entity.method.orEmpty(),
                    headers = entity.requestHeaders.orEmpty(),
                    body = entity.requestBody.orEmpty(),
                ),
                responseData = ResponseData(
                    code = entity.code.orZero(),
                    headers = entity.responseHeaders.orEmpty(),
                    body = entity.responseBody.orEmpty(),
                )
            )
        }
    }

    override suspend fun getAllAsJson(): Result<String> {
        val adapter = moshi.adapter(WebServer.Wrapper::class.java)
        return runCatching {
            val data = mockRepository.getAll().map { entity ->
                MockData(
                    uid = entity.uid,
                    isActive = entity.isActive,
                    requestData = RequestData(
                        url = entity.url.orEmpty(),
                        method = entity.method.orEmpty(),
                        headers = entity.requestHeaders.orEmpty(),
                        body = entity.requestBody.orEmpty(),
                    ),
                    responseData = ResponseData(
                        code = entity.code.orZero(),
                        headers = entity.responseHeaders.orEmpty(),
                        body = entity.responseBody.orEmpty(),
                    )
                )
            }
            adapter.toJson(WebServer.Wrapper(data))
        }
    }

    override suspend fun find(
        url: String,
        method: String,
        requestBody: String,
    ): MockData? {
        Log.d("###", "find: u:$url m:$method b:$requestBody")
        val entity = mockRepository.find(url, method, requestBody) ?: return null
        return MockData(
            uid = entity.uid,
            isActive = entity.isActive,
            requestData = RequestData(
                url = entity.url.orEmpty(),
                method = entity.method.orEmpty(),
                headers = entity.requestHeaders.orEmpty(),
                body = entity.requestBody.orEmpty(),
            ),
            responseData = ResponseData(
                code = entity.code.orZero(),
                headers = entity.responseHeaders.orEmpty(),
                body = entity.responseBody.orEmpty(),
            )
        )
    }

    override suspend fun insert(mockData: MockData) {
        mockRepository.insert(
            MockEntity(
                url = mockData.requestData.url,
                method = mockData.requestData.method,
                requestHeaders = mockData.requestData.headers,
                requestBody = mockData.requestData.body,
                responseHeaders = mockData.responseData.headers,
                responseBody = mockData.responseData.body.toString(),
                code = mockData.responseData.code,
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
