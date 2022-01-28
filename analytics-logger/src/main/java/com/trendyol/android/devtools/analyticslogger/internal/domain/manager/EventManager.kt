package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import kotlinx.coroutines.flow.Flow

internal interface EventManager {

    fun getAll(): Flow<List<Event>>

    suspend fun insert(event: Event)

    suspend fun delete(event: Event)
}
