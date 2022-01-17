package com.trendyol.android.devtools.httpinspector.internal.data.repository

import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import kotlinx.coroutines.flow.Flow

interface MockRepository {

    fun getAll(): Flow<List<MockEntity>>

    suspend fun insert(mockEntity: MockEntity)
}
