package com.trendyol.devtools.deeplinklauncher.internal.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkHistoryUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DeepLinkListViewModel(
    private val deeplinkHistoryUseCase: DeepLinkHistoryUseCase,
) : ViewModel() {
    private val listLiveData = MutableLiveData<List<String>>()

    fun getDeepLinkList(): LiveData<List<String>> = listLiveData

    private var tabIndex : Int? = null

    fun getDeepLinks() {
        viewModelScope.launch {
            if (tabIndex == 0){
                deeplinkHistoryUseCase.getHistory().collect {
                    listLiveData.value = it
                }
            }
            else if (tabIndex == 1){
                //todo get exported list
            }
        }
    }

    fun setTabIndex(int: Int?) {
        tabIndex = int
    }
}
