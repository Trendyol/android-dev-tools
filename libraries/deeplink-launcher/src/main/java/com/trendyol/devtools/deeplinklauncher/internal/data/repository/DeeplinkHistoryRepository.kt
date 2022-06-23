package com.trendyol.devtools.deeplinklauncher.internal.data.repository

import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeeplinkHistoryEntity
import kotlinx.coroutines.flow.Flow

internal interface DeeplinkHistoryRepository {

    suspend fun getHistory(): Flow<List<DeeplinkHistoryEntity>>

    suspend fun insert(deeplinkHistoryEntity: DeeplinkHistoryEntity)

    suspend fun deleteOldRecords()
}
