package com.trendyol.devtools.deeplinklauncher.internal.domain

import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeeplinkHistoryEntity
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeeplinkHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DeeplinkHistoryUseCaseImpl(
    private val deeplinkHistoryRepository: DeeplinkHistoryRepository,
) : DeeplinkHistoryUseCase {

    override suspend fun getHistory(): Flow<List<String>> {
        return deeplinkHistoryRepository.getHistory().map { list ->
            list.map {
                it.deeplink
            }
        }
    }

    override suspend fun insert(deeplink: String) {
        deeplinkHistoryRepository.insert(DeeplinkHistoryEntity(deeplink = deeplink))
    }

    override suspend fun deleteOldRecords() {
        deeplinkHistoryRepository.deleteOldRecords()
    }
}
