package com.trendyol.android.devtools.httpinspector.internal.domain.manager

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import com.trendyol.android.devtools.httpinspector.internal.domain.model.mock.MockData
import com.trendyol.android.devtools.httpinspector.internal.ext.mapToMockData
import com.trendyol.android.devtools.httpinspector.internal.ext.mapToMockEntity

internal class MockManagerImpl(
    private val mockRepository: MockRepository,
    private val moshi: Moshi,
) : MockManager {

    override suspend fun getAll(): List<MockData> {
        return mockRepository.getAll().map { entity -> entity.mapToMockData() }
    }

    override suspend fun getAllAsJson(): String? {
        val adapter = moshi.adapter<List<MockData>>(
            Types.newParameterizedType(List::class.java, MockData::class.java)
        )
        return runCatching { adapter.toJson(getAll()) }.getOrNull()
    }

    override suspend fun find(
        url: String,
        method: String,
        requestBody: String,
    ): MockData? {
        val entity = mockRepository.find(url, method, requestBody) ?: return null
        return entity.mapToMockData()
    }

    override suspend fun insert(mockData: MockData) {
        mockRepository.insert(mockData.mapToMockEntity())
    }

    override suspend fun delete(uid: Int) {
        mockRepository.delete(uid)
    }

    override suspend fun setActive(uid: Int, isActive: Boolean) {
        mockRepository.setActive(uid, isActive)
    }
}
