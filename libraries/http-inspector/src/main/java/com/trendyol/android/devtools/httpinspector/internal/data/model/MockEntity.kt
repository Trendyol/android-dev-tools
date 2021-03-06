package com.trendyol.android.devtools.httpinspector.internal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mock_entities")
internal data class MockEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "method") val method: String?,
    @ColumnInfo(name = "requestHeaders") val requestHeaders: String?,
    @ColumnInfo(name = "requestBody") val requestBody: String?,
    @ColumnInfo(name = "responseHeaders") val responseHeaders: String?,
    @ColumnInfo(name = "responseBody") val responseBody: String?,
    @ColumnInfo(name = "code") val code: Int?,
    @ColumnInfo(name = "isActive") val isActive: Boolean = true,
)
