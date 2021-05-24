package com.trendyol.devtools.internal.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.trendyol.devtools.internal.domain.EnvironmentUseCase
import com.trendyol.devtools.internal.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

internal class MainViewModel(private val environmentUseCase: EnvironmentUseCase) : ViewModel() {

    private val showEnvironmentSelectionLiveEvent: SingleLiveEvent<List<Pair<Boolean, String>>> = SingleLiveEvent()

    fun getShowEnvironmentSelectionLiveEvent(): LiveData<List<Pair<Boolean, String>>> =
        showEnvironmentSelectionLiveEvent

    init {
        onEnvironmentChangeClicked()
    }

    fun onEnvironmentChangeClicked() {
        environmentUseCase.getEnvironmentPairs()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ environments ->
                showEnvironmentSelectionLiveEvent.value = environments
            }, {
                TODO("Handle error")
            })
    }

    fun onEnvironmentSelected(environmentIndex: Int) {
        environmentUseCase.updateCurrentEnvironment(environmentIndex)
    }
}
