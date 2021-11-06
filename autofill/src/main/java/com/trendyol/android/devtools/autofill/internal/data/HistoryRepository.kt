package com.trendyol.android.devtools.autofill.internal.data

import com.trendyol.android.devtools.autofill.internal.model.ListItem

internal interface HistoryRepository {

    suspend fun save(fields: List<String>, item: ListItem.Autofill)

    suspend fun getLast(fields: List<String>): ListItem.Autofill?
}
