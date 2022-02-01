package com.trendyol.android.devtools.httpinspector.internal.data.repository

import com.trendyol.android.devtools.httpinspector.internal.data.database.MockDatabase
import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity

class MockRepositoryImpl(private val mockDatabase: MockDatabase) : MockRepository {

    override suspend fun getAll(): List<MockEntity> {
        return mockDatabase.mockDao().getAll()
    }

    override suspend fun find(
        url: String,
        method: String,
        requestBody: String,
    ): MockEntity? {
        return mockDatabase.mockDao().find(url, method, requestBody)
    }

    override suspend fun insert(mockEntity: MockEntity) {
        return mockDatabase.mockDao().insert(mockEntity)
    }

    override suspend fun delete(uid: Int) {
        return mockDatabase.mockDao().delete(uid)
    }

    override suspend fun setActive(uid: Int, isActive: Boolean) {
        return mockDatabase.mockDao().setActive(uid, isActive)
    }
}
