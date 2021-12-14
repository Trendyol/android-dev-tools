package com.trendyol.devtools.environmentmanager.internal.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.trendyol.android.devtools.core.lifecycle.SingleLiveEvent
import com.trendyol.devtools.environmentmanager.internal.domain.EnvironmentUseCase
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
            .subscribe(
                { environments ->
                    showEnvironmentSelectionLiveEvent.value = environments
                },
                {
                    TODO("Handle error")
                }
            )
    }

    fun onEnvironmentSelected(environmentIndex: Int) {
        environmentUseCase.updateCurrentEnvironment(environmentIndex)
    }
}
