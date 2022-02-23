package com.trendyol.devtools.environmentmanager.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.environmentmanager.internal.ui.MainViewModel

internal class MainContainer(private val environmentContainer: EnvironmentContainer) {

    inner class MainViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(environmentUseCase = environmentContainer.environmentUseCase) as T
        }
    }
}
