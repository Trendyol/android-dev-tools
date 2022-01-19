package com.trendyol.android.devtools.httpinspector.internal

import android.content.Context
import com.trendyol.android.devtools.core.io.FileReader
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ImportFrame
import com.trendyol.android.devtools.httpinspector.internal.domain.usecase.GetMockDataUseCase
import com.trendyol.android.devtools.httpinspector.internal.domain.usecase.SaveMockDataUseCase
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

internal class WebServer(
    private val context: Context,
    private val scope: CoroutineScope,
    private val getMockDataUseCase: GetMockDataUseCase,
    private val saveMockDataUseCase: SaveMockDataUseCase,
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
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
        get("/mock") {
            val fileData = FileReader.readAssetFile(this@WebServer.context, "mock.html")
            call.respondText(
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
    }

    private fun Routing.handleApi() = with(this) {
        get("/mock-data") {
            val mockData = getMockDataUseCase.invoke().first()
            call.respondText(
                contentType = ContentType.Application.Json,
                text = mockData.toString()
            )
        }
        post("/mock-data") {

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
