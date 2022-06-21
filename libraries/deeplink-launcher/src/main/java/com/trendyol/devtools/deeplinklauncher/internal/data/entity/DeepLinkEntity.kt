package com.trendyol.devtools.deeplinklauncher.internal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deeplink_entities")
internal data class DeeplinkEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "deeplink") val deeplink: String?
)
