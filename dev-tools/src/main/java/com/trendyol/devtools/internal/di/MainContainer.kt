package com.trendyol.devtools.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.internal.ui.MainViewModel

internal class MainContainer(private val environmentContainer: EnvironmentContainer) {

    inner class MainViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(
                environments = environmentContainer.environments,
                environmentUseCase = environmentContainer.provideGetCurrentEnvironmentUseCase()
            ) as T
        }
    }
}
