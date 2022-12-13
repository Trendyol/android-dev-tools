package com.trendyol.devtools.deeplinklauncher.internal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkHistoryUseCase
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkValidator
import com.trendyol.devtools.deeplinklauncher.internal.domain.EmptyNameException
import com.trendyol.devtools.deeplinklauncher.internal.domain.NotValidDeepLinkException
import com.trendyol.devtools.deeplinklauncher.internal.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val deeplinkHistoryUseCase: DeepLinkHistoryUseCase,
    private val deeplinkValidator: DeepLinkValidator
) : ViewModel() {
    private val notValidDeepLinkEvent = SingleLiveEvent<Int>()
    private val launchDeepLinkEvent = SingleLiveEvent<String>()

    fun getNotValidDeepLinkEvent(): SingleLiveEvent<Int> = notValidDeepLinkEvent
    fun getLaunchDeepLinkEvent(): SingleLiveEvent<String> = launchDeepLinkEvent

    init {
        deleteOldRecords()
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

    private fun deleteOldRecords() {
        viewModelScope.launch {
            deeplinkHistoryUseCase.deleteOldRecords()
        }
    }
}
