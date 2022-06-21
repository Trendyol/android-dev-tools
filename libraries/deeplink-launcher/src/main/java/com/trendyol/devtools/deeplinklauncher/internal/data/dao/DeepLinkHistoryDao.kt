package com.trendyol.devtools.deeplinklauncher.internal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeeplinkEntity

@Dao
internal interface DeepLinkHistoryDao {
    @Query("SELECT * FROM deeplink_entities ORDER BY uid DESC")
    suspend fun get(): List<DeeplinkEntity>

    @Insert
    suspend fun insert(vararg deeplinkEntity: DeeplinkEntity)
}
