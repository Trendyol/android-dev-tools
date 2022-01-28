package com.trendyol.android.devtools.httpinspector.internal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity

@Dao
interface MockDao {

    @Query("SELECT * FROM mock_entities")
    suspend fun getAll(): List<MockEntity>

    @Insert
    suspend fun insert(vararg mockEntities: MockEntity)

    @Query("DELETE FROM mock_entities WHERE uid = :uid")
    suspend fun delete(uid: Int)

    @Query("UPDATE mock_entities SET isActive = :isActive WHERE uid = :uid")
    suspend fun setActive(uid: Int, isActive: Boolean)
}
