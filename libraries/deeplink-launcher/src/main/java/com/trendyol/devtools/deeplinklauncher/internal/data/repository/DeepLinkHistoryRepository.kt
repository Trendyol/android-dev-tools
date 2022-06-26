package com.trendyol.devtools.deeplinklauncher.internal.data.repository

import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeepLinkHistoryEntity
import kotlinx.coroutines.flow.Flow

internal interface DeepLinkHistoryRepository {

    suspend fun getHistory(): Flow<List<DeepLinkHistoryEntity>>

    suspend fun insert(deeplinkHistoryEntity: DeepLinkHistoryEntity)

    suspend fun deleteOldRecords()
}
