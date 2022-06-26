package com.trendyol.devtools.deeplinklauncher.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.deeplinklauncher.internal.ui.list.DeepLinkListViewModel

internal class DeepLinkListContainer(
    private val deeplinkContainer: DeepLinkContainer
) {

    inner class DeepLinkListViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DeepLinkListViewModel(deeplinkContainer.deeplinkHistoryUseCase) as T
        }
    }
}
