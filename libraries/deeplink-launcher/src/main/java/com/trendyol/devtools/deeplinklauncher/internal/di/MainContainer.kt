package com.trendyol.devtools.deeplinklauncher.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.deeplinklauncher.internal.ui.MainViewModel

internal class MainContainer(
    private val deeplinkContainer: DeeplinkContainer
    ) {

    inner class MainViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(deeplinkContainer.deeplinkHistoryUseCase, deeplinkContainer.deeplinkValidator) as T
        }
    }
}
