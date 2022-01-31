package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.trendyol.android.devtools.analyticslogger.api.platform.EventPlatform
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import java.text.SimpleDateFormat
import java.util.*

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
                platform = eventEntity.platform,
                date = eventEntity.date,
            )
        }
    }

    override suspend fun insert(
        key: String?,
        value: String?,
        platform: EventPlatform?,
    ) {
        val dateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = dateFormat.format(Calendar.getInstance().time)

        return eventRepository.insert(
            EventEntity(
                key = key,
                value = value,
                platform = platform,
                date = date,
            )
        )
    }

    override suspend fun delete(uid: Int) {
        return eventRepository.delete(uid)
    }
}
