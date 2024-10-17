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

    override suspend fun find(query: String?, platform: String, page: Int, pageSize: Int): List<Event> {
        val events = eventRepository.find(
            query = "%${query.orEmpty()}%",
            platform = "%${platform}%",
            limit = PAGE_LIMIT,
            offset = calculateOffset(page),
        )
        return mapEventData(events)
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

    override suspend fun getPlatforms(): List<String> {
        return eventRepository.getPlatforms()
    }

    override suspend fun filterByPlatform(platform: String, page: Int, pageSize: Int): List<Event> {
        val events = eventRepository.filterByPlatform(
            platform = platform,
            limit = PAGE_LIMIT,
            offset = calculateOffset(page)
        )
        return mapEventData(events)
    }

    private fun calculateOffset(page: Int) = (page - 1) * PAGE_LIMIT

    private fun mapEventData(eventEntities: List<EventEntity>): List<Event> =
        eventEntities.map { eventEntity ->
            Event(
                uid = eventEntity.uid,
                key = eventEntity.key,
                value = eventEntity.value,
                json = eventEntity.value.beautify(moshi),
                platform = eventEntity.platform,
                date = eventEntity.date,
            )
        }

    companion object {
        private const val PAGE_LIMIT = 20
    }
}
