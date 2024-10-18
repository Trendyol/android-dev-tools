package com.trendyol.android.devtools.analyticslogger.internal.data.repository

import com.trendyol.android.devtools.analyticslogger.internal.data.database.EventDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

internal class EventRepositoryImpl(
    private val eventDatabase: EventDatabase,
) : EventRepository {

    override suspend fun find(query: String, platform: String, limit: Int, offset: Int): List<EventEntity> {
        return eventDatabase.eventDao().find(query, platform, limit, offset)
    }

    override suspend fun insert(eventEntity: EventEntity) {
        return eventDatabase.eventDao().insert(eventEntity)
    }

    override suspend fun deleteAll() {
        return eventDatabase.eventDao().deleteAll()
    }

    override suspend fun getPlatforms(): List<String> {
        val platforms = mutableListOf("All")
        platforms.addAll(eventDatabase.eventDao().getPlatforms())
        return platforms
    }

    override suspend fun filterByPlatform(platform: String, limit: Int, offset: Int): List<EventEntity> {
        return eventDatabase.eventDao().filterByPlatform(platform, limit, offset)
    }
}
