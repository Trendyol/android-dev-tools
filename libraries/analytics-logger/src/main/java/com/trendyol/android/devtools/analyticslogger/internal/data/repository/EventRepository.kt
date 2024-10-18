package com.trendyol.android.devtools.analyticslogger.internal.data.repository

import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

internal interface EventRepository {

    suspend fun find(query: String, platform: String, limit: Int, offset: Int): List<EventEntity>

    suspend fun insert(eventEntity: EventEntity)

    suspend fun deleteAll()

    suspend fun getPlatforms(): List<String>

    suspend fun filterByPlatform(platform: String, limit: Int, offset: Int): List<EventEntity>
}
