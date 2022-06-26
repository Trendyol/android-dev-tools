package com.trendyol.devtools.deeplinklauncher.internal.domain

import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeepLinkHistoryEntity
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.DeepLinkHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DeepLinkHistoryUseCaseImpl(
    private val deeplinkHistoryRepository: DeepLinkHistoryRepository,
) : DeepLinkHistoryUseCase {

    override suspend fun getHistory(): Flow<List<String>> {
        return deeplinkHistoryRepository.getHistory().map { list ->
            list.map {
                it.deeplink
            }
        }
    }

    override suspend fun insert(deeplink: String) {
        deeplinkHistoryRepository.insert(DeepLinkHistoryEntity(deeplink = deeplink))
    }

    override suspend fun deleteOldRecords() {
        deeplinkHistoryRepository.deleteOldRecords()
    }
}
