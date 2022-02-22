package com.trendyol.android.devtools.httpinspector.internal.domain.controller

import com.trendyol.android.devtools.httpinspector.internal.domain.manager.MockManager
import com.trendyol.android.devtools.httpinspector.internal.domain.model.request.AddMockRequest
import com.trendyol.android.devtools.httpinspector.internal.domain.model.mock.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import com.trendyol.android.devtools.httpinspector.internal.ext.orZero
import com.trendyol.android.devtools.httpinspector.internal.ext.respondBadRequest
import com.trendyol.android.devtools.httpinspector.internal.ext.respondSuccess
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

internal class HttpControllerImpl(
    private val mockManager: MockManager,
) : HttpController {

    override suspend fun getMockData(call: ApplicationCall) {
        val mockData = mockManager.getAllAsJson()
        if (mockData.isNullOrEmpty()) {
            return call.respondBadRequest()
        }
        call.respondSuccess(mockData)
    }

    override suspend fun saveMockData(call: ApplicationCall) {
        val request = call.receive<AddMockRequest>()

        mockManager.insert(
            MockData(
                requestData = RequestData(
                    request.url.orEmpty(),
                    request.method.orEmpty(),
                    request.requestHeaders.orEmpty(),
                    request.requestBody.orEmpty(),
                ),
                responseData = ResponseData(
                    request.code.orZero(),
                    request.responseHeaders,
                    request.responseBody,
                )
            )
        )
        call.respondSuccess()
    }

    override suspend fun deleteMockData(call: ApplicationCall) {
        val uid = call.parameters["uid"]

        if (uid.isNullOrEmpty()) {
            return call.respondBadRequest()
        }
        mockManager.delete(uid.toInt())
        call.respondSuccess()
    }

    override suspend fun setActive(call: ApplicationCall) {
        val uid = call.parameters["uid"].orEmpty().toIntOrNull().orZero()
        val isActive = call.parameters["isActive"]

        mockManager.setActive(uid, isActive == "true")
        call.respondSuccess()
    }
}
