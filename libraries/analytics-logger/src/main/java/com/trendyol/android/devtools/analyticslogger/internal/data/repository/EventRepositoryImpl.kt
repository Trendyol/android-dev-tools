package com.trendyol.android.devtools.analyticslogger.internal.data.repository

import com.trendyol.android.devtools.analyticslogger.internal.data.database.EventDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

internal class EventRepositoryImpl(
    private val eventDatabase: EventDatabase,
) : EventRepository {

    override suspend fun find(query: String, limit: Int, offset: Int): List<EventEntity> {
        return eventDatabase.eventDao().find(query, limit, offset)
    }

    override suspend fun insert(eventEntity: EventEntity) {
        return eventDatabase.eventDao().insert(eventEntity)
    }

    override suspend fun deleteAll() {
        return eventDatabase.eventDao().deleteAll()
    }
}
