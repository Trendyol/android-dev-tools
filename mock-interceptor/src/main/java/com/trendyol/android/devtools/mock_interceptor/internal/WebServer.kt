package com.trendyol.android.devtools.mock_interceptor.internal

import android.content.Context
import com.trendyol.android.devtools.core.io.FileReader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

internal class WebServer(
    private val context: Context,
    private val scope: CoroutineScope,
) {

    private val sessions = mutableListOf<DefaultWebSocketServerSession>()

    val incomingFlow = MutableSharedFlow<String>()

    val ongoingFlow = MutableSharedFlow<String>()

    init {
        setupWebServer()
        collectOngoingData()
    }

    fun hasConnection(): Boolean {
        return sessions.isNotEmpty()
    }

    private fun setupWebServer() = scope.launch {
        embeddedServer(CIO, PORT, watchPaths = emptyList()) {
            install(WebSockets)
            routing {
                handleWeb()
                handleWebSocket()
            }
        }.start(wait = true)
    }

    private fun collectOngoingData() = scope.launch {
        ongoingFlow.collect { json ->
            sessions.forEach { session ->
                session.send(json)
            }
        }
    }

    private fun Routing.handleWeb() = get(PATH_INDEX) {
        val fileData = FileReader.readAssetFile(this@WebServer.context, FILE_NAME_INDEX)
        call.respondText(
            text = fileData.orEmpty(),
            contentType = ContentType.Text.Html,
        )
    }

    private fun Routing.handleWebSocket() = webSocket(PATH_WS) {
        sessions.add(this)
        incoming.consumeAsFlow()
            .onCompletion {
                sessions.remove(this@webSocket)
            }
            .collect { frame ->
                if (frame is Frame.Text) {
                    incomingFlow.emit(frame.readText())
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
