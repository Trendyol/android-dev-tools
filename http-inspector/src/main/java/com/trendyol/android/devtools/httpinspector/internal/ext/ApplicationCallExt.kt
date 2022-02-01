package com.trendyol.android.devtools.httpinspector.internal.ext

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText

private const val JSON_SUCCESS = "{\"status\": \"ok\"}"
private const val JSON_ERROR = "{\"status\": \"error\"}"

internal suspend fun ApplicationCall.respondSuccess(data: String? = null) {
    respondText(
        status = HttpStatusCode.OK,
        contentType = ContentType.Application.Json,
        text = data ?: JSON_SUCCESS,
    )
}

internal suspend fun ApplicationCall.respondBadRequest(data: String? = null) {
    respondText(
        status = HttpStatusCode.BadRequest,
        contentType = ContentType.Application.Json,
        text = data ?: JSON_ERROR,
    )
}
