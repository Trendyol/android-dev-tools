package com.trendyol.android.devtools.httpinspector.internal.domain.controller

import io.ktor.application.ApplicationCall

interface HttpController {

    suspend fun getMockData(call: ApplicationCall)

    suspend fun saveMockData(call: ApplicationCall)

    suspend fun deleteMockData(call: ApplicationCall)
}
