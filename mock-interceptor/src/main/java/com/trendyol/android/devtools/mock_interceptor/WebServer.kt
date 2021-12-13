package com.trendyol.android.devtools.mock_interceptor

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.time.Duration

class WebServer(private val context: Context) {

    companion object {
        private const val PORT = 5001
    }

    val incomingFlow = MutableSharedFlow<String>()

    val ongoingFlow = MutableSharedFlow<String>()

    val sessions = mutableListOf<DefaultWebSocketServerSession>()

    val hasSession get() = sessions.isNotEmpty()

    private fun Routing.handleWeb() = get("/") {
        val f = FileReader.readAssetFile(this@WebServer.context, "control.html")
        call.respondText(
            text = f.orEmpty(),
            contentType = ContentType.Text.Html,
        )
    }

    private fun Routing.handleWebSocket() = webSocket("/echo") {
        sessions.add(this)
        Log.d("###", "connected")
        launch {
            ongoingFlow.collect {
                send(it)
            }
        }

        incoming.consumeAsFlow()
            .onCompletion {
                Log.d("###", "end connection")
                sessions.remove(this@webSocket)
            }
            .collect { frame ->
                if (frame is Frame.Text) {
                    when (val receivedText = frame.readText()) {
                        else ->  incomingFlow.emit(receivedText)
                    }
                }
            }
    }

    init {
        GlobalScope.launch {
            Log.d("###", "start server")
            embeddedServer(CIO, PORT, watchPaths = emptyList()) {
                install(WebSockets)
                routing {
                    handleWeb()
                    handleWebSocket()
                }
            }.start(wait = true)
        }
    }
}
