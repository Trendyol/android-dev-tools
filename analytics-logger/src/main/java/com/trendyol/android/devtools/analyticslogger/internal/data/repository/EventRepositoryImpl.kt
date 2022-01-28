package com.trendyol.android.devtools.analyticslogger.internal.data.repository

import com.trendyol.android.devtools.analyticslogger.internal.data.database.EventDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity
import kotlinx.coroutines.flow.Flow

internal class EventRepositoryImpl(
    private val eventDatabase: EventDatabase,
) : EventRepository {

    override fun getAll(): Flow<List<EventEntity>> {
        return eventDatabase.eventDao().getAll()
    }

    override suspend fun insert(eventEntity: EventEntity) {
        return eventDatabase.eventDao().insert(eventEntity)
    }

    override suspend fun delete(uid: Int) {
        return eventDatabase.eventDao().delete(uid)
    }
}
