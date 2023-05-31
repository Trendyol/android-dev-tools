package com.trendyol.android.devtools.httpinspector.internal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trendyol.android.devtools.httpinspector.internal.data.dao.MockDao
import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity

@Database(entities = [MockEntity::class], version = 1)
internal abstract class MockDatabase : RoomDatabase() {

    abstract fun mockDao(): MockDao

    companion object {
        fun create(context: Context): MockDatabase {
            return Room.databaseBuilder(context, MockDatabase::class.java, "mock-database-5").build()
        }
    }
}
