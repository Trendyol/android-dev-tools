package com.trendyol.android.devtools.httpinspector.internal.domain.usecase

import com.trendyol.android.devtools.httpinspector.internal.data.model.MockEntity
import com.trendyol.android.devtools.httpinspector.internal.data.repository.MockRepository
import kotlinx.coroutines.flow.Flow

class GetMockDataUseCase(
    private val mockRepository: MockRepository,
) {

    operator fun invoke(): Flow<List<MockEntity>> {
        return mockRepository.getAll()
    }
}
