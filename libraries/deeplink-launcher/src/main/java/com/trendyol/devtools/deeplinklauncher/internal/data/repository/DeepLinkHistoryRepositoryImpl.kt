package com.trendyol.devtools.deeplinklauncher.internal.data.repository

import com.trendyol.devtools.deeplinklauncher.internal.data.database.DeepLinkDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeepLinkHistoryEntity
import kotlinx.coroutines.flow.Flow

internal class DeepLinkHistoryRepositoryImpl(
    private val deeplinkDatabase: DeepLinkDatabase,
) : DeepLinkHistoryRepository {

    override suspend fun getHistory(): Flow<List<DeepLinkHistoryEntity>> {
        return deeplinkDatabase.historyDao().get()
    }

    override suspend fun insert(deeplinkHistoryEntity: DeepLinkHistoryEntity) {
        return deeplinkDatabase.historyDao().insertOrUpdate(deeplinkHistoryEntity)
    }

    override suspend fun deleteOldRecords() {
        deeplinkDatabase.historyDao().deleteOldRecords()
    }
}
