package com.trendyol.android.devtools.analyticslogger.internal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.analyticslogger.internal.data.dao.EventDao
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

@Database(entities = [EventEntity::class], version = 1)
@TypeConverters(PlatformConverter::class)
internal abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        fun create(context: Context, moshi: Moshi): EventDatabase {
            return Room.databaseBuilder(
                context,
                EventDatabase::class.java, "event-database-5"
            )
                .addTypeConverter(PlatformConverter(moshi))
                .build()
        }
    }
}
