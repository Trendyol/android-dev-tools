package com.trendyol.android.devtools.httpinspector.internal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MockDao {

    @Query("SELECT * FROM mock_entities")
    fun getAll(): Flow<List<MockEntity>>

    @Insert
    suspend fun insert(vararg mockEntities: MockEntity)

    @Delete
    suspend fun delete(mockEntity: MockEntity)
}
