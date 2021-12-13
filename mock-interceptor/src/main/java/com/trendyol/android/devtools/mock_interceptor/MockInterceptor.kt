package com.trendyol.android.devtools.mock_interceptor

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.mock_interceptor.ext.readString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.concurrent.TimeUnit

class MockInterceptor(private val context: Context) : Interceptor {

    private val supervisorJob = SupervisorJob()

    private val interceptorScope = CoroutineScope(supervisorJob + Dispatchers.IO)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val webServer = WebServer(context)

    private val queue = RequestQueue()

    private val adapt = moshi.adapter(ReqRes::class.java)

    init {
        interceptorScope.launch {
            webServer.incomingFlow.collect {
                runCatching { adapt.fromJson(it) }.getOrNull()?.let {
                    queue.resume(it)
                }
            }
        }
        interceptorScope.launch {
            queue.queueChannel.receiveAsFlow().collect {
                webServer.ongoingFlow.emit(adapt.toJson(it))
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .withConnectTimeout(50000, TimeUnit.MILLISECONDS)
            .withReadTimeout(50000, TimeUnit.MILLISECONDS)
            .withWriteTimeout(50000, TimeUnit.MILLISECONDS)
            .request()

        val response = chain.proceed(request)

        if (webServer.hasSession.not()) {
            return response
        }

        val reqRes = queue.add(request.body.readString(), response.body.readString())

        val mockedProcess = runBlocking { queue.waitFor(reqRes.id) }

        return Response.Builder()
            .request(request)
            .message(DEFAULT_RESPONSE_MESSAGE)
            .code(202)
            .protocol(Protocol.HTTP_2)
            .body(createResponseBody(MockResponse(200, mockedProcess.response)))
            .addHeader("content-type", CONTENT_TYPE_JSON)
            .apply {
               //addHeader
            }
            .build()
    }

    private fun createResponseBody(mockResponse: MockResponse): ResponseBody {
        return moshi.adapter(Any::class.java)
            .toJson(mockResponse.body)
            .toByteArray()
            .toResponseBody(CONTENT_TYPE_JSON.toMediaTypeOrNull())
    }

    companion object {
        private const val CONTENT_TYPE_JSON = "application/json"
        private const val DEFAULT_RESPONSE_MESSAGE = ""
    }
}

data class MockResponse(val code: Int, val body: String)

data class ReqRes(val id: Int, val request: String, val response: String)
