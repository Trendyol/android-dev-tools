package com.trendyol.android.devtools.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.android.devtools.internal.debugmenu.DebugMenuUseCase
import com.trendyol.android.devtools.internal.debugmenu.DebugMenuViewModel

internal class DebugMenuContainer {
    val debugMenuUseCase by lazy { DebugMenuUseCase() }

    inner class DebugMenuViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DebugMenuViewModel(debugMenuUseCase) as T
        }
    }
}