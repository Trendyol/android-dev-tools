package com.trendyol.devtools.httpdebug.internal.ui.detail.request

import androidx.lifecycle.LiveData
import com.trendyol.devtools.common.BaseViewModel
import com.trendyol.devtools.common.SingleLiveEvent
import com.trendyol.devtools.httpdebug.internal.domain.GetRequestUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

internal class RequestTabViewModel(private val getRequestUseCase: GetRequestUseCase) : BaseViewModel() {

    private val requestLiveEvent: SingleLiveEvent<RequestTabViewState> = SingleLiveEvent()

    fun getRequestLiveEvent(): LiveData<RequestTabViewState> = requestLiveEvent

    fun getRequestDetails(id: String) {
        getRequestUseCase
            .getRequest(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { requestLiveEvent.value = RequestTabViewState(it) }
            .also(disposable::add)
    }
}
