package com.trendyol.android.devtools.httpinspector.internal.domain.controller

import android.util.Log
import com.trendyol.android.devtools.httpinspector.internal.domain.manager.MockManager
import com.trendyol.android.devtools.httpinspector.internal.domain.model.AddMockResponse
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondText

class HttpControllerImpl(
    private val mockManager: MockManager,
) : HttpController {

    override suspend fun getMockData(call: ApplicationCall) {
        val mockData = mockManager.getAllAsJson()
        if (mockData.isFailure) {
            call.respondText(
                status = HttpStatusCode.BadRequest,
                contentType = ContentType.Application.Json,
                text = ""
            )
            return
        }
        call.respondText(
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json,
            text = mockData.getOrNull().orEmpty()
        )
    }

    override suspend fun saveMockData(call: ApplicationCall) {
        val request = call.receive<AddMockResponse>()

        mockManager.insert(
            MockData(
                requestData = RequestData(
                    request.url.orEmpty(),
                    request.method.orEmpty(),
                    request.requestHeaders.orEmpty(),
                    request.requestBody.orEmpty(),
                ),
                responseData = ResponseData(
                    200,
                    request.responseHeaders,
                    request.responseBody,
                )
            )
        )
        call.respondText(
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json,
            text = "{\"status\": \"ok\"}"
        )
    }

    override suspend fun deleteMockData(call: ApplicationCall) {
        val uid = call.parameters["uid"]

        if (uid.isNullOrEmpty()) {
            call.respondText(
                status = HttpStatusCode.BadRequest,
                contentType = ContentType.Application.Json,
                text = "{\"status\": \"400\"}"
            )
            return
        }

        mockManager.delete(uid.toInt())

        call.respondText(
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json,
            text = "{\"status\": \"ok\"}"
        )
    }

    override suspend fun setActive(call: ApplicationCall) {
        val uid = call.parameters["uid"]
        val isActive = call.parameters["isActive"]

        Log.d("###", "isActive: $isActive")

        mockManager.setActive(uid.orEmpty().toInt(), isActive == "true")

        call.respondText(
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json,
            text = "{\"status\": \"ok\"}"
        )
    }
}
