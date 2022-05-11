package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.ext.beautify
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal class EventManagerImpl(
    private val eventRepository: EventRepository,
    private val moshi: Moshi,
) : EventManager {

    override suspend fun find(query: String?, page: Int, pageSize: Int): List<Event> {
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
                json = eventEntity.value.beautify(moshi),
                platform = eventEntity.platform,
                date = eventEntity.date,
            )
        }
    }

    override suspend fun insert(
        key: String?,
        value: String?,
        platform: String?,
    ) {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
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

    override suspend fun deleteAll() {
        return eventRepository.deleteAll()
    }
}
