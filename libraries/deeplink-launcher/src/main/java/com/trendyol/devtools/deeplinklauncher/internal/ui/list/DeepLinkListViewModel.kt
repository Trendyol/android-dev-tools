package com.trendyol.devtools.deeplinklauncher.internal.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trendyol.devtools.deeplinklauncher.internal.domain.AppDeepLinkUseCase
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkHistoryUseCase
import kotlinx.coroutines.launch

class DeepLinkListViewModel(
    private val deeplinkHistoryUseCase: DeepLinkHistoryUseCase,
    private val appDeepLinkUseCase: AppDeepLinkUseCase
) : ViewModel() {
    private val listLiveData = MutableLiveData<List<String>>()

    fun getDeepLinkList(): LiveData<List<String>> = listLiveData

    private var tabIndex: Int? = null

    fun getDeepLinks() {
        viewModelScope.launch {
            if (tabIndex == 0) {
                deeplinkHistoryUseCase.getHistory().collect {
                    listLiveData.value = it
                }
            } else if (tabIndex == 1) {
                appDeepLinkUseCase.getAppDeepLinks().collect {
                    listLiveData.value = it
                }
            }
        }
    }

    fun setTabIndex(int: Int?) {
        tabIndex = int
    }
}
