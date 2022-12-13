package com.trendyol.devtools.deeplinklauncher.internal.domain

import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList
import com.trendyol.devtools.deeplinklauncher.internal.data.repository.AppDeepLinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppDeepLinkUseCaseImpl(
    private val deepLinkRepository: AppDeepLinkRepository,
) : AppDeepLinkUseCase {

    override suspend fun getAppDeepLinks(): Flow<List<String>> {
        return deepLinkRepository.getAppDeepLinks().map {
            it.deepLinks
        }
    }

    override suspend fun insertAll(deeplinks: DeepLinkList) {
        deepLinkRepository.insert(deeplinks)
    }

    override suspend fun deleteAll() {
        deepLinkRepository.deleteAll()
    }
}
