package com.trendyol.devtools.httpdebug.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.httpdebug.internal.domain.GetRequestUseCase
import com.trendyol.devtools.httpdebug.internal.domain.GetResponseUseCase
import com.trendyol.devtools.httpdebug.internal.ui.detail.response.ResponseTabViewModel

internal class ResponseTabContainer {

    private val getResponseUseCase: GetResponseUseCase by lazy { GetResponseUseCase(MainContainer.manipulatorUseCase) }

    internal inner class ResponseTabViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ResponseTabViewModel(getResponseUseCase) as T
        }
    }
}
