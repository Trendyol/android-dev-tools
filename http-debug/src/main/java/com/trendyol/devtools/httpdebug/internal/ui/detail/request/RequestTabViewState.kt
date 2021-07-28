package com.trendyol.devtools.httpdebug.internal.ui.detail.request

import android.content.Context
import com.trendyol.devtools.httpdebug.R
import com.trendyol.devtools.httpdebug.internal.model.HttpDebugRequest

internal data class RequestTabViewState(private val request: HttpDebugRequest) {

    fun getHeaders(): String = TODO()

    fun getRequestBody(context: Context): String {
        if (request.requestBody == null || request.requestBody.isDuplex() || request.requestBody.isOneShot() || request.requestBody.contentType()?.type != "text") {
            return context.getString(R.string.dev_tools_http_debug_string_request_body_not_presentable)
        }

        TODO()
    }
}
