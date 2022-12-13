package com.trendyol.devtools.deeplinklauncher.internal.domain

import kotlinx.coroutines.flow.Flow

interface DeepLinkHistoryUseCase {
    suspend fun getHistory(): Flow<List<String>>

    suspend fun insert(deeplink: String)

    suspend fun deleteOldRecords()
}
