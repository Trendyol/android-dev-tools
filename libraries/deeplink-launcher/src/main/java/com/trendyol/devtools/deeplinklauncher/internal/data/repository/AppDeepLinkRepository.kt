package com.trendyol.devtools.deeplinklauncher.internal.data.repository

import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList
import kotlinx.coroutines.flow.Flow

internal interface AppDeepLinkRepository {

    suspend fun getAppDeepLinks(): Flow<DeepLinkList>

    suspend fun insert(appDeepLinkEntity: DeepLinkList)

    suspend fun deleteAll()
}
