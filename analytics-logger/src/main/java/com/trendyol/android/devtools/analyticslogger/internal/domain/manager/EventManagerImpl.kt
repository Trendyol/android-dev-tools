package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class EventManagerImpl(
    private val eventRepository: EventRepository,
) : EventManager {

    override fun getAll(): Flow<List<Event>> {
        return eventRepository.getAll().map { events ->
            events.map { eventEntity ->
                Event(
                    uid = eventEntity.uid,
                    key = eventEntity.key,
                    value = eventEntity.value,
                )
            }
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
