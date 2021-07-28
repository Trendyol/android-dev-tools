package com.trendyol.devtools.httpdebug.internal.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trendyol.devtools.common.BaseViewModel
import com.trendyol.devtools.httpdebug.internal.domain.GetActiveRequestsUseCase
import com.trendyol.devtools.httpdebug.internal.domain.SkipRequestUseCase
import com.trendyol.devtools.httpdebug.internal.model.RequestResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

internal class ListViewModel(
    private val getActiveRequestsUseCase: GetActiveRequestsUseCase,
    private val skipRequestUseCase: SkipRequestUseCase
) : BaseViewModel() {

    private val requestsLiveData = MutableLiveData<List<RequestResponse>>()

    fun getRequestsLiveData(): LiveData<List<RequestResponse>> = requestsLiveData

    fun getRequests() {
        getActiveRequestsUseCase
            .getActiveRequests()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { requestsLiveData.value = it }
            .also(disposable::add)
    }

    fun skipRequest(id: String) {
        //skipRequestUseCase.skipRequest(id)
    }

    fun skipAllRequests() {
        skipRequestUseCase.skipAllRequests()
    }
}
