package com.trendyol.devtools.httpdebug.internal.domain

import com.trendyol.devtools.httpdebug.internal.model.HttpDebugRequest
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

internal class GetRequestUseCase(private val manipulatorUseCase: ManipulatorUseCase) {

    fun getRequest(id: String): Observable<HttpDebugRequest> {
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
                it?.request
            }
    }
}
