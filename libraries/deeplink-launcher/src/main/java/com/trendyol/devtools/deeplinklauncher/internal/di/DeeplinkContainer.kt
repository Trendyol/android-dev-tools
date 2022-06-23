package com.trendyol.devtools.deeplinklauncher.internal.di

import android.content.Context
import com.trendyol.devtools.deeplinklauncher.internal.data.database.DeeplinkDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeeplinkHistoryRepository
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeeplinkHistoryRepositoryImpl
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeeplinkHistoryUseCase
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeeplinkHistoryUseCaseImpl
import com.trendyol.devtools.deeplinklauncher.internal.domain.DeeplinkValidator

internal class DeeplinkContainer(private val context: Context) {
    private val deeplinkDatabase: DeeplinkDatabase by lazy { DeeplinkDatabase.create(context) }

    private val deeplinkHistoryRepository: DeeplinkHistoryRepository by lazy { DeeplinkHistoryRepositoryImpl(deeplinkDatabase) }

    val deeplinkHistoryUseCase: DeeplinkHistoryUseCase by lazy { DeeplinkHistoryUseCaseImpl(deeplinkHistoryRepository) }

    val deeplinkValidator: DeeplinkValidator by lazy { DeeplinkValidator() }
}
