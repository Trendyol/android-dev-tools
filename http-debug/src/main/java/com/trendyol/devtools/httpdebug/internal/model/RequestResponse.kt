package com.trendyol.devtools.httpdebug.internal.model

import android.view.View

internal data class RequestResponse(
    val id: String,
    var request: HttpDebugRequest,
    var response: HttpDebugResponse
) {

    fun getResponseCodeVisibility(): Int = if (response.response != null) View.VISIBLE else View.GONE
}
