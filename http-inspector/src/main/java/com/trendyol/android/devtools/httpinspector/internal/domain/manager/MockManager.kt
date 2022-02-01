package com.trendyol.android.devtools.httpinspector.internal.domain.manager

import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData

interface MockManager {

    suspend fun getAll(): List<MockData>

    suspend fun getAllAsJson(): Result<String>

    suspend fun find(
        url: String,
        method: String,
        requestBody: String,
    ): MockData?

    suspend fun insert(mockData: MockData)

    suspend fun delete(uid: Int)

    suspend fun setActive(uid: Int, isActive: Boolean)
}
