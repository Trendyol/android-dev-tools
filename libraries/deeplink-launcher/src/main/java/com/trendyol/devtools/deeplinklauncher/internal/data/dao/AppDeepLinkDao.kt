package com.trendyol.devtools.deeplinklauncher.internal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.AppDeepLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AppDeepLinkDao {
    @Query("SELECT * FROM app_deeplink_entities")
    fun getDeepLinks(): Flow<AppDeepLinkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg appDeepLinks: AppDeepLinkEntity)

    @Query("DELETE FROM app_deeplink_entities")
    suspend fun deleteAll()
}
