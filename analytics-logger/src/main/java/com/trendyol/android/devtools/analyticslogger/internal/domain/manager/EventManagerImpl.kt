package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.analyticslogger.api.platform.EventPlatform
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.ext.beautify
import java.text.SimpleDateFormat
import java.util.*

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
        value: Any?,
        platform: EventPlatform?,
    ) {
        val dateFormat = SimpleDateFormat("hh:mm:SS", Locale.getDefault())
        val date = dateFormat.format(Calendar.getInstance().time)

        val adapter = moshi.adapter(Any::class.java)
        val jsonValue = runCatching { adapter.toJson(value) }.getOrNull()

        return eventRepository.insert(
            EventEntity(
                key = key,
                value = jsonValue,
                platform = platform,
                date = date,
            )
        )
    }

    override suspend fun deleteAll() {
        return eventRepository.deleteAll()
    }
}
