package com.trendyol.devtools.httpdebug.internal.domain

import com.trendyol.devtools.httpdebug.internal.model.HttpDebugRequest
import com.trendyol.devtools.httpdebug.internal.model.HttpDebugResponse
import com.trendyol.devtools.httpdebug.internal.model.RequestResponse
import com.trendyol.devtools.httpdebug.internal.model.State
import io.reactivex.rxjava3.subjects.BehaviorSubject
import okhttp3.Request
import okhttp3.Response

internal class ManipulatorUseCase {

    private var toggle: Boolean = false
    val requestsSubject: BehaviorSubject<MutableMap<String, RequestResponse>> =
        BehaviorSubject.createDefault(mutableMapOf())

    fun newRequest(request: Request, id: String): Request {
        requestsSubject.value[id] = RequestResponse(
            id = id,
            request = HttpDebugRequest(request, if (toggle) State.BLOCKED else State.PASS),
            response = HttpDebugResponse(null, State.BLOCKED)
        )
        requestsSubject.onNext(requestsSubject.value)

        while (requestsSubject.value[id]?.request?.state == State.BLOCKED) {
            Thread.sleep(100)
        }

        return requestsSubject.value[id]?.request?.request ?: request
    }

    fun newResponse(response: Response, id: String): Response {
        requestsSubject.value[id]?.response =
            HttpDebugResponse(response, if (toggle) State.BLOCKED else State.PASS)

        while (requestsSubject.value[id]?.response?.state == State.BLOCKED) {
            Thread.sleep(100)
        }

        return requestsSubject.value[id]?.response?.response ?: response
    }

    fun setToggleToTrue() {
        toggle = true
    }

    fun setToggleToFalse() {
        toggle = false
        requestsSubject.value.clear()
    }

    fun interceptRequest(id: String, modifyBlock: (Request) -> Request) {
        requestsSubject.value[id]?.request?.request = modifyBlock.invoke(requestsSubject.value[id]!!.request.request)
        requestsSubject.value[id]?.request?.state = State.SENT
    }

    fun interceptResponse(id: String, modifyBlock: (Response) -> Response) {
        requestsSubject.value[id]?.response?.response =
            modifyBlock.invoke(requestsSubject.value[id]!!.response.response!!)
        requestsSubject.value[id]?.request?.state = State.SENT
        requestsSubject.value[id]?.response?.state = State.SENT
    }
}
