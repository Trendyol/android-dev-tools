package com.trendyol.devtools.autofill.internal.data

import com.trendyol.devtools.autofill.internal.model.ListItemEntity

interface HistoryRepository {

    suspend fun save(entity: ListItemEntity)

    suspend fun getLast(): ListItemEntity?
}
