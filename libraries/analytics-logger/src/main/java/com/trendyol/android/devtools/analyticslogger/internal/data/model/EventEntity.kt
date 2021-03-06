package com.trendyol.android.devtools.analyticslogger.internal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_entities")
internal data class EventEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "key") val key: String?,
    @ColumnInfo(name = "value") val value: String?,
    @ColumnInfo(name = "platform") val platform: String?,
    @ColumnInfo(name = "date") val date: String?,
)
