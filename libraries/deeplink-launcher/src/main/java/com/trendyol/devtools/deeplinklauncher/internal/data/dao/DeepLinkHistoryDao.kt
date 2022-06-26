package com.trendyol.devtools.deeplinklauncher.internal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeepLinkHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DeepLinkHistoryDao {
    @Query("SELECT * FROM deeplink_entities ORDER BY uid DESC")
    fun get(): Flow<List<DeepLinkHistoryEntity>>

    @Query("SELECT * FROM deeplink_entities WHERE deeplink = :deeplinkString LIMIT 1")
    suspend fun getEntityByDeepLink(deeplinkString: String): DeepLinkHistoryEntity?

    @Insert
    suspend fun insert(vararg deeplinkEntity: DeepLinkHistoryEntity)

    @Delete
    suspend fun delete(vararg deeplinkEntity: DeepLinkHistoryEntity)

    @Query("DELETE FROM deeplink_entities where uid NOT IN (SELECT uid from deeplink_entities ORDER BY uid DESC LIMIT 40)")
    suspend fun deleteOldRecords()

    suspend fun insertOrUpdate(deeplinkEntity: DeepLinkHistoryEntity) {
        val historyItem : DeepLinkHistoryEntity? = getEntityByDeepLink(deeplinkEntity.deeplink)
        if (historyItem != null){
            delete(historyItem)
        }
        insert(deeplinkEntity)
    }
}
