package com.trendyol.devtools.deeplinklauncher.internal.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeeplinkHistoryUseCase
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeeplinkValidator
import com.trendyol.devtools.deeplinklauncher.internal.domain.EmptyNameException
import com.trendyol.devtools.deeplinklauncher.internal.domain.NotValidDeepLinkException
import com.trendyol.devtools.deeplinklauncher.internal.lifecycle.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val deeplinkHistoryUseCase: DeeplinkHistoryUseCase,
    private val deeplinkValidator: DeeplinkValidator
) : ViewModel() {

    private val historyLiveData = MutableLiveData<List<String>>()
    private val notValidDeepLinkEvent = SingleLiveEvent<Int>()
    private val launchDeepLinkEvent = SingleLiveEvent<String>()

    fun getHistory(): LiveData<List<String>> = historyLiveData
    fun getNotValidDeepLinkEvent(): SingleLiveEvent<Int> = notValidDeepLinkEvent
    fun getLaunchDeepLinkEvent(): SingleLiveEvent<String> = launchDeepLinkEvent

    init {
        deleteOldRecords()
        getDeepLinks()
    }

    fun onDeepLinkFired(deepLink: String) {
        viewModelScope.launch {
            runCatching {
                deeplinkValidator.validate(deepLink)
            }.onSuccess {
                deeplinkHistoryUseCase.insert(deepLink)
                launchDeepLinkEvent.value = deepLink
            }.onFailure {
                var errorMessage: Int? = null
                when (it) {
                    is EmptyNameException -> errorMessage = R.string.deeplink_empty_error
                    is NotValidDeepLinkException -> errorMessage = R.string.deeplink_not_valid_error
                }
                errorMessage?.let {
                    notValidDeepLinkEvent.value = it
                }
            }
        }
    }

    private fun getDeepLinks() {
        viewModelScope.launch {
            deeplinkHistoryUseCase.getHistory().collect {
                historyLiveData.value = it
            }
        }
    }

    private fun deleteOldRecords() {
        viewModelScope.launch {
            deeplinkHistoryUseCase.deleteOldRecords()
        }
    }
}

