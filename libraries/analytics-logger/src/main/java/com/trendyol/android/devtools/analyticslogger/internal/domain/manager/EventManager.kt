package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

internal interface EventManager {

    suspend fun find(query: String?, platform: String, page: Int, pageSize: Int): List<Event>

    suspend fun insert(key: String?, value: String?, platform: String?)

    suspend fun deleteAll()

    suspend fun getPlatforms(): List<String>

    suspend fun filterByPlatform(platform: String, page: Int, pageSize: Int): List<Event>
}
