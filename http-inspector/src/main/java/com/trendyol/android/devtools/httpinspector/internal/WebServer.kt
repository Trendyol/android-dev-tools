package com.trendyol.android.devtools.httpinspector.internal

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.core.io.FileReader
import com.trendyol.android.devtools.httpinspector.internal.domain.manager.MockManager
import com.trendyol.android.devtools.httpinspector.internal.domain.model.AddMockResponse
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ImportFrame
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

internal class WebServer(
    private val context: Context,
    private val scope: CoroutineScope,
    private val moshi: Moshi,
    private val mockManager: MockManager,
) {

    private val sessions = mutableListOf<DefaultWebSocketServerSession>()

    private val importFlow = MutableSharedFlow<ImportFrame>()

    private val exportFlow = MutableSharedFlow<String>()

    init {
        setupWebServer()
        collectOngoingData()
    }

    fun hasConnection(): Boolean {
        return sessions.isNotEmpty()
    }

    fun getImportFlow(): SharedFlow<ImportFrame> {
        return importFlow
    }

    fun getExportFlow(): MutableSharedFlow<String> {
        return exportFlow
    }

    private fun setupWebServer() = scope.launch {
        embeddedServer(CIO, PORT, watchPaths = emptyList()) {
            install(WebSockets)
            install(ContentNegotiation) {
                gson()
            }
            routing {
                handleWeb()
                handleApi()
                handleWebSocket()
            }
        }.start(wait = true)
    }

    private fun collectOngoingData() = scope.launch {
        exportFlow.collect { json ->
            sessions.forEach { session ->
                session.send(Frame.Text(json))
            }
        }
    }

    private fun Routing.handleWeb() = with(this) {
        get(PATH_INDEX) {
            val fileData = FileReader.readAssetFile(this@WebServer.context, FILE_NAME_INDEX)
            call.respondText(
                status = HttpStatusCode.OK,
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
        get("/mock.html") {
            val fileData = FileReader.readAssetFile(this@WebServer.context, "mock.html")
            call.respondText(
                status = HttpStatusCode.OK,
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
        get("/add-mock.html") {
            val fileData = FileReader.readAssetFile(this@WebServer.context, "add-mock.html")
            call.respondText(
                status = HttpStatusCode.OK,
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
    }

    data class Wrapper(val data: List<MockData>)

    private fun Routing.handleApi() = with(this) {
        get("/mock-data") {
            val adapter = moshi.adapter(Wrapper::class.java)
            val mockData = runCatching { mockManager.getAll() }.getOrElse {
                Log.d("###", "errr: $it")
                listOf()
            }
            call.respondText(
                status = HttpStatusCode.OK,
                contentType = ContentType.Application.Json,
                text = adapter.toJson(Wrapper(mockData))
            )
        }
        post("/add-mock") {
            val request = call.receive<AddMockResponse>()

            mockManager.insert(MockData(
                RequestData(
                    request.url.orEmpty(),
                    request.method.orEmpty(),
                    mapOf(),
                    request.requestBody.orEmpty(),
                ),
                ResponseData(
                    200,
                    mapOf(),
                    request.responseBody.orEmpty(),
                )
            ))
            call.respondText(
                status = HttpStatusCode.OK,
                contentType = ContentType.Application.Json,
                text = "{\"status\": \"ok\"}"
            )
        }
        post("/delete-mock") {
            val uid = call.parameters.get("uid")

        }
    }

    private fun Routing.handleWebSocket() = webSocket(PATH_WS) {
        sessions.add(this)
        incoming.consumeAsFlow()
            .onCompletion {
                sessions.remove(this@webSocket)
                importFlow.emit(ImportFrame.Close)
            }
            .collect { frame ->
                when (frame) {
                    is Frame.Text -> importFlow.emit(ImportFrame.Text(frame.readText()))
                    is Frame.Close -> importFlow.emit(ImportFrame.Close)
                    else -> {}
                }
            }
    }

    companion object {
        private const val PORT = 5001
        private const val PATH_INDEX = "/"
        private const val PATH_WS = "/echo"
        private const val FILE_NAME_INDEX = "control.html"
    }
}
