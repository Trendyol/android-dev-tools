package com.trendyol.android.devtools.analyticslogger.internal.data.repository

import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

internal interface EventRepository {

    suspend fun find(query: String, limit: Int, offset: Int): List<EventEntity>

    suspend fun insert(eventEntity: EventEntity)

    suspend fun delete(uid: Int)
}
