package com.trendyol.android.devtools.httpinspector.internal.domain.model

import com.google.gson.annotations.SerializedName

data class AddMockResponse(
    @SerializedName("url") val url: String?,
    @SerializedName("method") val method: String?,
    @SerializedName("requestHeaders") val requestHeaders: String?,
    @SerializedName("requestBody") val requestBody: String?,
    @SerializedName("responseHeaders") val responseHeaders: String?,
    @SerializedName("responseBody") val responseBody: String?,
)
