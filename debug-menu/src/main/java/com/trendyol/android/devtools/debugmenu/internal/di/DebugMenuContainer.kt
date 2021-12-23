package com.trendyol.android.devtools.debugmenu.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase
import com.trendyol.android.devtools.debugmenu.internal.ui.DebugMenuViewModel

internal class DebugMenuContainer {
    val debugMenuUseCase by lazy { DebugMenuUseCase() }

    inner class DebugMenuViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DebugMenuViewModel(debugMenuUseCase) as T
        }
    }
}
