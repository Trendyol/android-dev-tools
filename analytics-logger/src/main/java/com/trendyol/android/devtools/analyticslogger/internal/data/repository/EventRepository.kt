package com.trendyol.android.devtools.analyticslogger.internal.data.repository

import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity
import kotlinx.coroutines.flow.Flow

internal interface EventRepository {

    fun getAll(): Flow<List<EventEntity>>

    suspend fun insert(eventEntity: EventEntity)

    suspend fun delete(uid: Int)
}
