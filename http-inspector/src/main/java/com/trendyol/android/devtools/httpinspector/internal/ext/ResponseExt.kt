package com.trendyol.android.devtools.httpinspector.internal.ext

import com.trendyol.android.devtools.httpinspector.internal.util.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

internal fun ResponseBody?.readString(): String {
    return this?.string().orEmpty()
}

internal fun String?.toResponseBody(): ResponseBody {
    return orEmpty()
        .toByteArray()
        .toResponseBody(Constants.CONTENT_TYPE_JSON.toMediaTypeOrNull())
}
