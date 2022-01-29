package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

internal class EventManagerImpl(
    private val eventRepository: EventRepository,
) : EventManager {

    override suspend fun find(query: String?, page: Int): List<Event> {
        val limit = 20
        val offset = (page - 1) * limit

        val events = eventRepository.find(
            query = "%${query.orEmpty()}%",
            limit = limit,
            offset = offset,
        )
        return events.map { eventEntity ->
            Event(
                uid = eventEntity.uid,
                key = eventEntity.key,
                value = eventEntity.value,
            )
        }
    }

    override suspend fun insert(event: Event) {
        return eventRepository.insert(
            EventEntity(
                uid = event.uid,
                key = event.key,
                value = event.value,
            )
        )
    }

    override suspend fun delete(event: Event) {
        return eventRepository.delete(event.uid)
    }
}
