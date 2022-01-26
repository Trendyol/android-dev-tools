package com.trendyol.android.devtools.httpinspector.internal.domain.controller

import android.util.Log
import com.trendyol.android.devtools.httpinspector.internal.WebServer
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
        val adapter = moshi.adapter(WebServer.Wrapper::class.java)
        val mockData = runCatching { mockManager.getAll() }.getOrElse {
            Log.d("###", "errr: $it")
            listOf()
        }
        call.respondText(
            status = HttpStatusCode.OK,
            contentType = ContentType.Application.Json,
            text = adapter.toJson(WebServer.Wrapper(mockData))
        )
    }

    override suspend fun saveMockData(call: ApplicationCall) {
        val request = call.receive<AddMockResponse>()

        mockManager.insert(
            MockData(
                requestData = RequestData(
                    request.url.orEmpty(),
                    request.method.orEmpty(),
                    mapOf(),
                    request.requestBody.orEmpty(),
                ),
                responseData = ResponseData(
                    200,
                    mapOf(),
                    request.responseBody.orEmpty(),
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
}
