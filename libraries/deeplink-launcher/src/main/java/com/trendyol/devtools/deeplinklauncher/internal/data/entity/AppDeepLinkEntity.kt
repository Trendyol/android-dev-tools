package com.trendyol.devtools.deeplinklauncher.internal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList

@Entity(tableName = "app_deeplink_entities")
internal data class AppDeepLinkEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "deeplinkList") val deeplink: DeepLinkList
)
