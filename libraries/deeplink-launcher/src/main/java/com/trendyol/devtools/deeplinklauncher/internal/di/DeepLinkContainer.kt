package com.trendyol.devtools.deeplinklauncher.internal.di

import android.content.Context
import com.trendyol.devtools.deeplinklauncher.internal.data.database.DeepLinkDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeepLinkHistoryRepository
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeepLinkHistoryRepositoryImpl
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkHistoryUseCase
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkHistoryUseCaseImpl
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeepLinkValidator

internal class DeepLinkContainer(private val context: Context) {
    private val deeplinkDatabase: DeepLinkDatabase by lazy { DeepLinkDatabase.create(context) }

    private val deeplinkHistoryRepository: DeepLinkHistoryRepository by lazy { DeepLinkHistoryRepositoryImpl(deeplinkDatabase) }

    val deeplinkHistoryUseCase: DeepLinkHistoryUseCase by lazy { DeepLinkHistoryUseCaseImpl(deeplinkHistoryRepository) }

    val deeplinkValidator: DeepLinkValidator by lazy { DeepLinkValidator() }
}
