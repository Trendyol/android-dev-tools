package com.trendyol.android.devtools.httpinspector.internal.domain.manager

import com.trendyol.android.devtools.httpinspector.internal.domain.model.MockData

interface MockManager {

    suspend fun getAll(): Result<String>

    suspend fun insert(mockData: MockData)

    suspend fun delete(uid: Int)

    suspend fun setActive(uid: Int, isActive: Boolean)
}
