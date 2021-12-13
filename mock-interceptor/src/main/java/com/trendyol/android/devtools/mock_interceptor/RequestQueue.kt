package com.trendyol.android.devtools.mock_interceptor

import kotlinx.coroutines.channels.Channel
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RequestQueue {

    private var id = AtomicInteger(0)

    private val pendingList = mutableMapOf<Int, Continuation<ReqRes>>()

    private val queue = mutableListOf<ReqRes>()

    val queueChannel = Channel<ReqRes>()

    fun add(request: String, response: String): ReqRes {
        val reqRes = ReqRes(id.incrementAndGet(), request, response)
        if (queue.isEmpty()) queueChannel.trySend(reqRes)
        queue.add(reqRes)
        return reqRes
    }

    suspend fun waitFor(id: Int) = suspendCoroutine<ReqRes> { continuation ->
        synchronized(pendingList) {
            pendingList[id] = continuation
        }
    }

    fun resume(reqRes: ReqRes) {
        synchronized(pendingList) {
            pendingList[reqRes.id]?.resume(reqRes)
            pendingList.remove(reqRes.id)
        }
        synchronized(queue) {
            queue.remove(reqRes)
            queue.firstOrNull()?.let { next ->
                queueChannel.trySend(next)
            }
        }
    }
}
