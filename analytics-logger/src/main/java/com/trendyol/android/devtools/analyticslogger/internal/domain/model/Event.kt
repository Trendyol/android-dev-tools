package com.trendyol.android.devtools.analyticslogger.internal.domain.model

import com.trendyol.android.devtools.analyticslogger.EventPlatform

internal data class Event(
    val uid: Int,
    val key: String?,
    val value: String?,
    val json: String?,
    val platform: EventPlatform?,
    val date: String?,
)
