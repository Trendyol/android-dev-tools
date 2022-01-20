package com.trendyol.android.devtools.httpinspector.internal

import com.trendyol.android.devtools.httpinspector.internal.model.Carrier
import com.trendyol.android.devtools.httpinspector.internal.model.Information
import com.trendyol.android.devtools.httpinspector.internal.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.model.ResponseCarrier
import com.trendyol.android.devtools.httpinspector.internal.model.ResponseData
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

internal class RequestQueue {

    private var id = AtomicInteger(0)

    private val pendingList = mutableMapOf<Int, Continuation<ResponseData>>()

    private val queue = mutableListOf<Carrier>()

    private val queueChannel = Channel<Carrier>()

    fun getQueueChannel(): Channel<Carrier> {
        return queueChannel
    }

    fun add(
        requestData: RequestData,
        responseData: ResponseData,
        requestTimeInMillis: Long,
        responseTimeInMillis: Long
    ): Carrier {
        val carrier = Carrier(
            id.incrementAndGet(),
            requestData,
            responseData,
            requestTimeInMillis,
            responseTimeInMillis
        )
        if (queue.isEmpty()) queueChannel.trySend(carrier)
        queue.add(carrier)
        return carrier
    }

    suspend fun waitFor(id: Int) = suspendCoroutine<ResponseData> { continuation ->
        synchronized(pendingList) {
            pendingList[id] = continuation
        }
    }

    fun resume(responseCarrier: ResponseCarrier) {
        synchronized(pendingList) {
            pendingList[responseCarrier.id]?.resume(responseCarrier.responseData)
            pendingList.remove(responseCarrier.id)
        }
        synchronized(queue) {
            queue.removeAll { carrier -> carrier.id == responseCarrier.id }
            queue.firstOrNull()?.let { next ->
                queueChannel.trySend(next)
            }
        }
    }

    suspend fun cancel() {
        pendingList.forEach { pending ->
            val carrier = queue.firstOrNull { carrier -> carrier.id == pending.key }
            carrier?.let { pending.value.resume(it.responseData) }
            delay(200)
        }
        queue.clear()
        pendingList.clear()
    }
}
