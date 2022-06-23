package com.trendyol.devtools.deeplinklauncher.internal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeeplinkHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DeepLinkHistoryDao {
    @Query("SELECT * FROM deeplink_entities ORDER BY uid DESC")
    fun get(): Flow<List<DeeplinkHistoryEntity>>

    @Query("SELECT * FROM deeplink_entities WHERE deeplink = :deeplinkString LIMIT 1")
    suspend fun getEntityByDeeplink(deeplinkString: String): DeeplinkHistoryEntity?

    @Insert
    suspend fun insert(vararg deeplinkEntity: DeeplinkHistoryEntity)

    @Delete
    suspend fun delete(vararg deeplinkEntity: DeeplinkHistoryEntity)

    @Query("DELETE FROM deeplink_entities where uid NOT IN (SELECT uid from deeplink_entities ORDER BY uid DESC LIMIT 40)")
    suspend fun deleteOldRecords()

    suspend fun insertOrUpdate(deeplinkEntity: DeeplinkHistoryEntity) {
        val historyItem : DeeplinkHistoryEntity? = getEntityByDeeplink(deeplinkEntity.deeplink)
        if (historyItem != null){
            delete(historyItem)
        }
        insert(deeplinkEntity)
    }
}
