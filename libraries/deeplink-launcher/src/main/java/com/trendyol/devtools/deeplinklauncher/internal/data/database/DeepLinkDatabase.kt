package com.trendyol.devtools.deeplinklauncher.internal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.dao.DeepLinkHistoryDao
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeepLinkHistoryEntity

@Database(entities = [DeepLinkHistoryEntity::class], version = 1)
internal abstract class DeepLinkDatabase : RoomDatabase() {

    abstract fun historyDao(): DeepLinkHistoryDao

    companion object {
        fun create(context: Context): DeepLinkDatabase {
            return Room.databaseBuilder(
                context,
                DeepLinkDatabase::class.java, "devtool-deeplink-database"
            ).build()
        }
    }
}
