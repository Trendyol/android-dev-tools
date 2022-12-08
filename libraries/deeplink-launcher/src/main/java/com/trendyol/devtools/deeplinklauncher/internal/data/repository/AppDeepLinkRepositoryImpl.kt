package com.trendyol.devtools.deeplinklauncher.internal.data.repository

import com.trendyol.devtools.deeplinklauncher.internal.data.database.DeepLinkDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.AppDeepLinkEntity
import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppDeepLinkRepositoryImpl(
    private val deeplinkDatabase: DeepLinkDatabase,
) : AppDeepLinkRepository {

    override suspend fun getAppDeepLinks(): Flow<DeepLinkList> {
        return deeplinkDatabase.appDeepLinkDao().getDeepLinks().map {
            it.deeplink
        }
    }

    override suspend fun insert(deepLinkList: DeepLinkList) {
        return deeplinkDatabase.appDeepLinkDao().insert(
            AppDeepLinkEntity(deeplink = deepLinkList)
        )
    }

    override suspend fun deleteAll() {
        deeplinkDatabase.appDeepLinkDao().deleteAll()
    }
}
