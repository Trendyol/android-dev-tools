package com.trendyol.android.devtools.analyticslogger.internal.domain.model

import com.trendyol.android.devtools.analyticslogger.api.platform.EventPlatform

data class Event(
    val uid: Int,
    val key: String?,
    val value: String?,
    val platform: EventPlatform?,
    val date: String?,
)
