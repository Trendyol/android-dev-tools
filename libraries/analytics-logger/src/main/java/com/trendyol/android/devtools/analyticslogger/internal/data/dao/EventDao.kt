package com.trendyol.android.devtools.analyticslogger.internal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

@Dao
internal interface EventDao {

    @Query("SELECT * FROM event_entities WHERE `key` LIKE :query ORDER BY uid DESC LIMIT :limit OFFSET :offset")
    suspend fun find(query: String, limit: Int, offset: Int): List<EventEntity>

    @Insert
    suspend fun insert(vararg eventEntities: EventEntity)

    @Query("DELETE FROM event_entities")
    suspend fun deleteAll()
}
