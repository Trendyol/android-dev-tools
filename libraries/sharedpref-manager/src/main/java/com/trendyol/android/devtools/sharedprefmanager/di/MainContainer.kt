package com.trendyol.android.devtools.sharedprefmanager.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefUpdateTypeValidator
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefManagerViewModel

internal class MainContainer(
    private val sharedPrefUseCaseContainer: SharedPrefUseCaseContainer,
    private val sharedPrefUpdateTypeValidator: SharedPrefUpdateTypeValidator
) {

    inner class SharedPrefManagerViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SharedPrefManagerViewModel(
                sharedPrefManagerUseCase = sharedPrefUseCaseContainer.sharedPrefManagerUseCase,
                sharedPrefUpdateTypeValidator = sharedPrefUpdateTypeValidator
            ) as T
        }
    }
}
