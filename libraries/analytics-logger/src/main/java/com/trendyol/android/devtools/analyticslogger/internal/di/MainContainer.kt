package com.trendyol.android.devtools.analyticslogger.internal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel

internal class MainContainer(private val analyticsContainer: AnalyticsContainer) {

    inner class MainViewModelFactory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(eventManager = analyticsContainer.eventManager) as T
        }
    }
}
