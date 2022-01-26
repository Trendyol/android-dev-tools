package com.trendyol.android.devtools.httpinspector.internal.domain.manager

import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData

interface MockManager {

    suspend fun getAll(): List<MockData>

    suspend fun insert(mockData: MockData)

    suspend fun delete(uid: Int)
}
