package com.trendyol.devtools.deeplinklauncher.internal.domain

import kotlinx.coroutines.flow.Flow

interface DeeplinkHistoryUseCase {
    suspend fun getHistory(): Flow<List<String>>

    suspend fun insert(deeplink: String)

    suspend fun deleteOldRecords()
}
