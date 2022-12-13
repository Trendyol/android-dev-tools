package com.trendyol.devtools.deeplinklauncher.internal.domain

import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList
import kotlinx.coroutines.flow.Flow

interface AppDeepLinkUseCase {
    suspend fun getAppDeepLinks(): Flow<List<String>>

    suspend fun insertAll(deeplinks: DeepLinkList)

    suspend fun deleteAll()
}
