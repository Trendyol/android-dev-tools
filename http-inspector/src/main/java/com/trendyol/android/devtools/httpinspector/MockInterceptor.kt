package com.trendyol.android.devtools.httpinspector

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.httpinspector.internal.RequestQueue
import com.trendyol.android.devtools.httpinspector.internal.WebServer
import com.trendyol.android.devtools.httpinspector.internal.data.database.MockDatabase
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepositoryImpl
import com.trendyol.android.devtools.httpinspector.internal.domain.controller.HttpController
import com.trendyol.android.devtools.httpinspector.internal.domain.controller.HttpControllerImpl
import com.trendyol.android.devtools.httpinspector.internal.domain.manager.MockManager
import com.trendyol.android.devtools.httpinspector.internal.domain.manager.MockManagerImpl
import com.trendyol.android.devtools.httpinspector.internal.domain.model.Carrier
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ImportFrame
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseCarrier
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.mock.MockData
import com.trendyol.android.devtools.httpinspector.internal.ext.readString
import com.trendyol.android.devtools.httpinspector.internal.ext.safeParse
import com.trendyol.android.devtools.httpinspector.internal.ext.toHeaderMap
import com.trendyol.android.devtools.httpinspector.internal.ext.toHeaders
import com.trendyol.android.devtools.httpinspector.internal.ext.toJson
import com.trendyol.android.devtools.httpinspector.internal.ext.toResponseBody
import com.trendyol.android.devtools.httpinspector.internal.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.lang.StringBuilder
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MockInterceptor(context: Context) : Interceptor {

    private val supervisorJob = SupervisorJob()

    private val interceptorScope = CoroutineScope(supervisorJob + Dispatchers.IO)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val mockDatabase: MockDatabase by lazy { MockDatabase.create(context) }

    private val mockRepository: MockRepository by lazy { MockRepositoryImpl(mockDatabase) }

    private val mockManager: MockManager by lazy { MockManagerImpl(mockRepository, moshi) }

    private val httpController: HttpController by lazy { HttpControllerImpl(mockManager) }

    private val webServer = WebServer(context, interceptorScope, httpController)

    private val requestQueue = RequestQueue()

    init {
        collectSocket()
        collectRequestQueue()
    }

    private fun collectSocket() = interceptorScope.launch {
        val responseCarrierAdapter = moshi.adapter(ResponseCarrier::class.java)
        webServer.getImportFlow().collect { frame ->
            when (frame) {
                is ImportFrame.Text -> responseCarrierAdapter.safeParse(frame.content)?.let { responseCarrier ->
                    requestQueue.resume(responseCarrier)
                }
                is ImportFrame.Close -> requestQueue.cancel()
            }
        }
    }

    private fun collectRequestQueue() = interceptorScope.launch {
        val carrierAdapter = moshi.adapter(Carrier::class.java)
        requestQueue.getQueueChannel().receiveAsFlow().collect { carrier ->
            webServer.getExportFlow().emit(carrierAdapter.toJson(carrier))
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Find if there is a defined mock data related with this request.
        val mockData = runBlocking {
            mockManager.find(
                url = request.url.toString(),
                method = request.method,
                requestBody = request.body.readString(),
            )
        }

        // If there is a defined mock data, stop chain and return mock response.
        if (mockData != null) {
            return mockData.createOkHttpResponse(request)
        }

        // Continue with current chain if there is no connected web client.
        if (webServer.hasConnection().not()) {
            return chain.proceed(chain.request())
        }

        val requestWithTimeout = chain
            .withConnectTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .withReadTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .withWriteTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .request()

        val requestTimeInMillis = Calendar.getInstance().timeInMillis
        // Get the actual response.
        val response = chain.proceed(requestWithTimeout)
        val responseTimeInMillis = Calendar.getInstance().timeInMillis

        val carrier = requestQueue.add(
            requestData = RequestData(
                url = requestWithTimeout.url.toString(),
                method = requestWithTimeout.method,
                headers = requestWithTimeout.headers.toJson(moshi),
                body = requestWithTimeout.body.readString(),
            ),
            responseData = ResponseData(
                code = response.code,
                headers = response.headers.toJson(moshi),
                body = response.body.readString(),
            ),
            requestTimeInMillis = requestTimeInMillis,
            responseTimeInMillis = responseTimeInMillis,
            cURL = request.toCurl()
        )

        // Wait manipulated response from the web client.
        val responseData = runBlocking { requestQueue.waitFor(carrier.id) }

        return Response.Builder()
            .request(requestWithTimeout)
            .message(DEFAULT_RESPONSE_MESSAGE)
            .code(responseData.code)
            .protocol(Protocol.HTTP_2)
            .body(responseData.body.toResponseBody())
            .headers(responseData.headers.toHeaderMap(moshi).toHeaders())
            .build()
    }

    private fun MockData.createOkHttpResponse(request: Request): Response {
        return Response.Builder()
            .request(request)
            .message(DEFAULT_RESPONSE_MESSAGE)
            .code(responseData.code)
            .protocol(Protocol.HTTP_2)
            .body(responseData.body.toResponseBody())
            .addHeader(HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_JSON)
            .apply {
                responseData.headers.toHeaderMap(moshi).forEach { pair ->
                    pair.value.forEach { value -> addHeader(pair.key, value) }
                }
            }
            .build()
    }

    private fun Request.toCurl(): String {
        return StringBuilder().apply {
            append("curl -X ${this@toCurl.method}")
            this@toCurl.headers.forEach { header ->
                append(" -H \"${header.first}: ${header.second}\"")
            }
            append(" ")
            append(this@toCurl.url.toString())
        }.toString()
    }

    companion object {
        private const val HEADER_CONTENT_TYPE = "content-type"
        private const val DEFAULT_RESPONSE_MESSAGE = ""
        private const val REQUEST_TIMEOUT = 50000
    }
}
