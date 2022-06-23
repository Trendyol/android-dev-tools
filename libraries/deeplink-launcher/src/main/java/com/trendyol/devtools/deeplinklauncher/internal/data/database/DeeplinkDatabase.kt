package com.trendyol.devtools.deeplinklauncher.internal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trendyol.devtools.deeplinklauncher.internal.data.dao.DeepLinkHistoryDao
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeeplinkHistoryEntity

@Database(entities = [DeeplinkHistoryEntity::class], version = 1)
internal abstract class DeeplinkDatabase : RoomDatabase() {

    abstract fun historyDao(): DeepLinkHistoryDao

    companion object {
        fun create(context: Context): DeeplinkDatabase {
            return Room.databaseBuilder(
                context,
                DeeplinkDatabase::class.java, "devtool-deeplink-database"
            ).build()
        }
    }
}
