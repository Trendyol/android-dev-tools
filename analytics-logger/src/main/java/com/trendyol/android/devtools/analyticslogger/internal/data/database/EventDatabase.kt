package com.trendyol.android.devtools.analyticslogger.internal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.dao.EventDao
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

@Database(entities = [EventEntity::class], version = 1)
internal abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        fun create(context: Context): EventDatabase {
            return Room.databaseBuilder(
                context,
                EventDatabase::class.java, "event-database-1"
            ).build()
        }
    }
}
