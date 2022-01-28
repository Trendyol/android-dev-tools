package com.trendyol.android.devtools.analyticslogger.internal.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

@Dao
internal interface EventDao {

    @Query("SELECT * FROM event_entities")
    fun getAll(): PagingSource<Int, EventEntity>

    @Insert
    suspend fun insert(vararg eventEntities: EventEntity)

    @Query("DELETE FROM event_entities WHERE uid = :uid")
    suspend fun delete(uid: Int)
}
