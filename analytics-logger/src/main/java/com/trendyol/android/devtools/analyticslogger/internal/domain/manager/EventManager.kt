package com.trendyol.android.devtools.analyticslogger.internal.domain.manager

import com.trendyol.android.devtools.analyticslogger.api.platform.EventPlatform
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

internal interface EventManager {

    suspend fun find(query: String?, page: Int, pageSize: Int): List<Event>

    suspend fun insert(key: String?, value: String?, platform: EventPlatform?)

    suspend fun deleteAll()
}
