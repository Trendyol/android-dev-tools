package com.trendyol.devtools.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.internal.ui.MainViewModel

internal class MainContainer(private val environmentContainer: EnvironmentContainer) {

    fun getViewModel(): MainViewModel = MainViewModelFactory().create(MainViewModel::class.java)

    inner class MainViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(
                environments = environmentContainer.environments,
                environmentUseCase = environmentContainer.provideGetCurrentEnvironmentUseCase()
            ) as T
        }
    }
}
