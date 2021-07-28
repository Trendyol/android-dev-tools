package com.trendyol.devtools.httpdebug.internal.domain

import com.trendyol.devtools.httpdebug.internal.model.RequestResponse
import com.trendyol.devtools.httpdebug.internal.model.State
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

internal class GetActiveRequestsUseCase(private val manipulatorUseCase: ManipulatorUseCase) {

    fun getActiveRequests(): Observable<List<RequestResponse>> {
        return manipulatorUseCase
            .requestsSubject
            .subscribeOn(Schedulers.io())
            .concatMap {
                Observable.fromIterable(it.values)
                    .filter { requestResponse ->
                        requestResponse.response != null && requestResponse.response!!.state == State.BLOCKED
                    }
                    .toList()
                    .toObservable()
            }
    }
}
