package com.trendyol.devtools.httpdebug.internal.ui.detail.response

import androidx.lifecycle.LiveData
import com.trendyol.devtools.common.BaseViewModel
import com.trendyol.devtools.common.SingleLiveEvent
import com.trendyol.devtools.httpdebug.internal.domain.GetResponseUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

internal class ResponseTabViewModel(private val getResponseUseCase: GetResponseUseCase) : BaseViewModel() {

    private val responseLiveEvent: SingleLiveEvent<ResponseTabViewState> = SingleLiveEvent()

    fun getResponseLiveEvent(): LiveData<ResponseTabViewState> = responseLiveEvent

    fun getResponse(id: String) {
        getResponseUseCase
            .getResponse(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { responseLiveEvent.value = ResponseTabViewState(it) }
            .also(disposable::add)
    }
}
