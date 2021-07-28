package com.trendyol.devtools.httpdebug.internal.domain

internal class SkipRequestUseCase(
    private val manipulatorUseCase: ManipulatorUseCase
) {

    fun skipRequest(id: String, isRequest: Boolean) {
        if (isRequest) {
            manipulatorUseCase.interceptRequest(id) { it }
        } else {
            manipulatorUseCase.interceptResponse(id) { it }
        }
        TODO("skip request with given id $id")
    }

    fun skipAllRequests() {
        TODO("skip all requests")
    }
}
