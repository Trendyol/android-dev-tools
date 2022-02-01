package com.trendyol.android.devtools.httpinspector.internal.domain.model.request

import com.google.gson.annotations.SerializedName

internal data class AddMockRequest(
    @SerializedName("url") val url: String?,
    @SerializedName("method") val method: String?,
    @SerializedName("requestHeaders") val requestHeaders: String?,
    @SerializedName("requestBody") val requestBody: String?,
    @SerializedName("responseHeaders") val responseHeaders: String?,
    @SerializedName("responseBody") val responseBody: String?,
    @SerializedName("code") val code: Int?,
)
