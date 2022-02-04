package com.trendyol.android.devtools.analyticslogger.internal.domain.model

internal data class Event(
    val uid: Int,
    val key: String?,
    val value: String?,
    val json: String?,
    val platform: String?,
    val date: String?,
)
