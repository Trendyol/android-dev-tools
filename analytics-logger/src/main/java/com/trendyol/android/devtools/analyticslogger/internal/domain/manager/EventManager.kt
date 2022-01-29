package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

internal interface EventManager {

    suspend fun find(query: String?, page: Int): List<Event>

    suspend fun insert(event: Event)

    suspend fun delete(event: Event)
}
