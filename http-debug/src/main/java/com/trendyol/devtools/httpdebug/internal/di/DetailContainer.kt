package com.trendyol.devtools.httpdebug.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.httpdebug.internal.ui.detail.DetailViewModel

internal class DetailContainer {

    internal inner class DetailViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailViewModel(MainContainer.manipulatorUseCase) as T
        }
    }
}
