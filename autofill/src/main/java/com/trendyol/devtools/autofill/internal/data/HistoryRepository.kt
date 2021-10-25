package com.trendyol.devtools.autofill.internal.data

import com.trendyol.devtools.autofill.internal.model.AutofillEntity
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    suspend fun save(entity: AutofillEntity)

    suspend fun getLast(): AutofillEntity

    suspend fun getAll(): List<AutofillEntity>
}
