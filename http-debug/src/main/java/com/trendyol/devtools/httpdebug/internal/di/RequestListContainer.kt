package com.trendyol.devtools.httpdebug.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.httpdebug.internal.domain.GetActiveRequestsUseCase
import com.trendyol.devtools.httpdebug.internal.domain.SkipRequestUseCase
import com.trendyol.devtools.httpdebug.internal.ui.list.ListViewModel

internal class RequestListContainer {

    val getActiveRequestsUseCase by lazy { GetActiveRequestsUseCase(MainContainer.manipulatorUseCase) }
    val skipRequestUseCase by lazy { SkipRequestUseCase(MainContainer.manipulatorUseCase) }

    internal inner class ListViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ListViewModel(
                getActiveRequestsUseCase,
                skipRequestUseCase
            ) as T
    }
}
