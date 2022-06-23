package com.trendyol.devtools.deeplinklauncher.internal.data.repository

import com.trendyol.devtools.deeplinklauncher.internal.data.database.DeeplinkDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeeplinkHistoryEntity
import kotlinx.coroutines.flow.Flow

internal class DeeplinkHistoryRepositoryImpl(
    private val deeplinkDatabase: DeeplinkDatabase,
) : DeeplinkHistoryRepository {

    override suspend fun getHistory(): Flow<List<DeeplinkHistoryEntity>> {
        return deeplinkDatabase.historyDao().get()
    }

    override suspend fun insert(deeplinkHistoryEntity: DeeplinkHistoryEntity) {
        return deeplinkDatabase.historyDao().insertOrUpdate(deeplinkHistoryEntity)
    }

    override suspend fun deleteOldRecords() {
        deeplinkDatabase.historyDao().deleteOldRecords()
    }
}
