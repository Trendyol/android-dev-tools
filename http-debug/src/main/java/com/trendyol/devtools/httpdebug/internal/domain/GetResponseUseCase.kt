package com.trendyol.devtools.httpdebug.internal.domain

import com.trendyol.devtools.httpdebug.internal.model.HttpDebugResponse
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

internal class GetResponseUseCase(private val manipulatorUseCase: ManipulatorUseCase) {

    fun getResponse(id: String): Observable<HttpDebugResponse> {
        return manipulatorUseCase
            .requestsSubject
            .subscribeOn(Schedulers.io())
            .flatMapMaybe {
                if (it[id] != null) {
                    Maybe.just(it[id])
                } else {
                    Maybe.empty()
                }
            }
            .map {
                it?.response
            }
    }
}
