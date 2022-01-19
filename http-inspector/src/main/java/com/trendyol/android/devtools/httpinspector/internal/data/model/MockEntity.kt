package com.trendyol.android.devtools.httpinspector.internal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mock_entities")
data class MockEntity(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "method") val method: String?,
    @ColumnInfo(name = "requestBody") val requestBody: String?,
    @ColumnInfo(name = "responseBody") val responseBody: String?,
)
