package com.trendyol.devtools.deeplinklauncher.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.deeplinklauncher.internal.ui.list.DeeplinkListViewModel

internal class DeepLinkListContainer(
    private val deeplinkContainer: DeeplinkContainer
) {

    inner class DeeplinkListViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DeeplinkListViewModel(deeplinkContainer.deeplinkHistoryUseCase) as T
        }
    }
}
