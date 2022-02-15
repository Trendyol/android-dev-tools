package com.trendyol.android.devtools.httpinspector.internal.data.repository

import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity

internal interface MockRepository {

    suspend fun getAll(): List<MockEntity>

    suspend fun find(
        url: String,
        method: String,
        requestBody: String,
    ): MockEntity?

    suspend fun insert(mockEntity: MockEntity)

    suspend fun delete(uid: Int)

    suspend fun setActive(uid: Int, isActive: Boolean)
}
