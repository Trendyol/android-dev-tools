package com.trendyol.devtools.httpdebug.internal.model

import okhttp3.Request
import okhttp3.RequestBody

internal data class HttpDebugRequest(var request: Request, var state: State) {

    val requestUrl: String = request.url.toString()
    val requestMethod: String = request.method
    val requestBody: RequestBody? = request.body

    fun getHeaders(): Map<String, List<String>> = request.headers.toMultimap()
}
