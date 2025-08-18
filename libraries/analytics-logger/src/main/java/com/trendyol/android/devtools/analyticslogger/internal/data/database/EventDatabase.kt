package com.trendyol.android.devtools.analyticslogger.internal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.dao.EventDao
import com.trendyol.android.devtools.analyticslogger.internal.data.model.EventEntity

@Database(entities = [EventEntity::class], version = 3)
internal abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        fun create(context: Context): EventDatabase {
            return Room.databaseBuilder(
                context,
                EventDatabase::class.java,
                "analytics-logger-database-1",
            )
                .addMigrations(*migrations)
                .build()
        }

        private val migrations = arrayOf(
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE event_entities ADD COLUMN isSuccess INTEGER")
                }
            },
            object : Migration(2, 3) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE event_entities ADD COLUMN source TEXT")
                }
            },
        )
    }
}
