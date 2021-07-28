package com.trendyol.devtools.httpdebug.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.httpdebug.internal.domain.GetRequestUseCase
import com.trendyol.devtools.httpdebug.internal.ui.detail.request.RequestTabViewModel

internal class RequestTabContainer {

    private val getRequestUseCase: GetRequestUseCase by lazy { GetRequestUseCase(MainContainer.manipulatorUseCase) }

    internal inner class RequestTabViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RequestTabViewModel(getRequestUseCase) as T
        }
    }
}
