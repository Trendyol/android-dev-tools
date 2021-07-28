package com.trendyol.devtools.httpdebug

import com.trendyol.devtools.httpdebug.internal.di.MainContainer
import com.trendyol.devtools.httpdebug.internal.domain.IdGeneratorUseCase
import com.trendyol.devtools.httpdebug.internal.domain.ManipulatorUseCase
import okhttp3.Interceptor
import okhttp3.Response

class HttpDebugInterceptor : Interceptor {

    private val useCase: ManipulatorUseCase = MainContainer.manipulatorUseCase
    private val requestIdGeneratorUseCase: IdGeneratorUseCase = MainContainer.idGeneratorUseCase

    override fun intercept(chain: Interceptor.Chain): Response {
        val id: String = requestIdGeneratorUseCase.generateId(chain.request())

        val updatedRequest = useCase.newRequest(chain.request(), id)

        return useCase.newResponse(chain.proceed(updatedRequest), id)
    }
}
