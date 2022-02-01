package com.trendyol.android.devtools.httpinspector

import android.content.Context
import android.util.Log
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
import com.trendyol.android.devtools.httpinspector.internal.ext.readString
import com.trendyol.android.devtools.httpinspector.internal.ext.safeParse
import com.trendyol.android.devtools.httpinspector.internal.ext.toHeaderMap
import com.trendyol.android.devtools.httpinspector.internal.ext.toHeaders
import com.trendyol.android.devtools.httpinspector.internal.ext.toJson
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
import okhttp3.ResponseBody.Companion.toResponseBody
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

    private val webServer = WebServer(
        context,
        interceptorScope,
        moshi,
        mockManager,
        httpController,
    )

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
        val r = chain.request()
        val requestBody = r.body.readString()

        val mockRequest = runBlocking {
            mockManager.find(
                url = r.url.toString(),
                method = r.method,
                requestBody = requestBody,
            )
        }

        if (mockRequest != null) {
            return Response.Builder()
                .request(r)
                .message(DEFAULT_RESPONSE_MESSAGE)
                .code(mockRequest.responseData.code)
                .protocol(Protocol.HTTP_2)
                .body(mockRequest.responseData.body.orEmpty().toByteArray().toResponseBody(CONTENT_TYPE_JSON.toMediaTypeOrNull()))
                .addHeader("content-type", CONTENT_TYPE_JSON)
                .apply {
                    mockRequest.responseData.headers.toHeaderMap(moshi).forEach { pair ->
                        addHeader(pair.key, pair.value.first())
                    }
                }
                .build()
        }

        if (webServer.hasConnection().not()) {
            return chain.proceed(chain.request())
        }

        val request = chain
            .withConnectTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .withReadTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .withWriteTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
            .request()

        val response = chain.proceed(request)

        val carrier = requestQueue.add(
            requestData = RequestData(
                url = request.url.toString(),
                method = request.method,
                headers = request.headers.toJson(moshi),
                body = request.body.readString(),
            ),
            responseData = ResponseData(
                code = response.code,
                headers = response.headers.toJson(moshi),
                body = response.body.readString(),
            ),
        )

        val responseData = runBlocking { requestQueue.waitFor(carrier.id) }

        return Response.Builder()
            .request(request)
            .message(DEFAULT_RESPONSE_MESSAGE)
            .code(responseData.code)
            .protocol(Protocol.HTTP_2)
            .body(responseData.body.orEmpty().toByteArray().toResponseBody(CONTENT_TYPE_JSON.toMediaTypeOrNull()))
            .headers(responseData.headers.toHeaderMap(moshi).toHeaders())
            .build()
    }


    companion object {
        private const val CONTENT_TYPE_JSON = "application/json"
        private const val DEFAULT_RESPONSE_MESSAGE = ""
        private const val REQUEST_TIMEOUT = 50000
    }
}
