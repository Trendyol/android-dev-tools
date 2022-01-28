package com.trendyol.android.devtools.httpinspector.internal

import android.content.Context
import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.httpinspector.internal.domain.controller.HttpController
import com.trendyol.android.devtools.httpinspector.internal.domain.manager.MockManager
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ImportFrame
import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData
import com.trendyol.android.devtools.httpinspector.internal.router.ApiRouter
import com.trendyol.android.devtools.httpinspector.internal.router.StaticRouter
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Routing
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
    private val httpController: HttpController,
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
            install(ContentNegotiation) { gson() }
            StaticRouter(context).run(this)
            ApiRouter(httpController).run(this)
            routing { handleWebSocket() }
        }.start(wait = true)
    }

    private fun collectOngoingData() = scope.launch {
        exportFlow.collect { json ->
            sessions.forEach { session ->
                session.send(Frame.Text(json))
            }
        }
    }

    data class Wrapper(val data: List<MockData>)


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
        private const val PATH_WS = "/echo"
    }
}
