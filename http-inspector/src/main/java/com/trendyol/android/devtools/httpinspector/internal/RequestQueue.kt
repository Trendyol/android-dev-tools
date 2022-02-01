package com.trendyol.android.devtools.httpinspector.internal

import com.trendyol.android.devtools.httpinspector.internal.domain.model.Carrier
import com.trendyol.android.devtools.httpinspector.internal.domain.model.RequestData
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseCarrier
import com.trendyol.android.devtools.httpinspector.internal.domain.model.ResponseData
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
    ): Carrier {
        val carrier = Carrier(id.incrementAndGet(), requestData, responseData)
        synchronized(queue) {
            if (queue.isEmpty()) queueChannel.trySend(carrier)
            queue.add(carrier)
        }
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

    fun cancel() {
        synchronized(pendingList) {
            pendingList.forEach { pending ->
                val carrier = queue.firstOrNull { carrier -> carrier.id == pending.key }
                carrier?.let { pending.value.resume(it.responseData) }
            }
            pendingList.clear()
        }
        synchronized(queue) {
            queue.clear()
        }
    }
}
