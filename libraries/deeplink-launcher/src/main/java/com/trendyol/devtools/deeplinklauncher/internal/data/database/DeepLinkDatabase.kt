package com.trendyol.devtools.deeplinklauncher.internal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.trendyol.devtools.deeplinklauncher.internal.data.converter.AppDeepLinkListConverter
import com.trendyol.devtools.deeplinklauncher.internal.data.dao.AppDeepLinkDao
import com.trendyol.devtools.deeplinklauncher.internal.data.dao.DeepLinkHistoryDao
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.AppDeepLinkEntity
import com.trendyol.devtools.deeplinklauncher.internal.data.entity.DeepLinkHistoryEntity

@Database(
    entities = [
        DeepLinkHistoryEntity::class,
        AppDeepLinkEntity::class
    ], version = 1
)
@TypeConverters(
    AppDeepLinkListConverter::class
)
internal abstract class DeepLinkDatabase : RoomDatabase() {

    abstract fun historyDao(): DeepLinkHistoryDao

    abstract fun appDeepLinkDao(): AppDeepLinkDao

    companion object {
        fun create(context: Context): DeepLinkDatabase {
            return Room.databaseBuilder(
                context,
                DeepLinkDatabase::class.java, "devtool-deeplink-database"
            ).build()
        }
    }
}
