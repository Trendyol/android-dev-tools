package com.trendyol.android.devtools.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.android.devtools.internal.main.MainViewModel

internal class MainContainer(private val environmentContainer: EnvironmentContainer) {

    inner class MainViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(environmentUseCase = environmentContainer.environmentUseCase) as T
        }
    }
}
