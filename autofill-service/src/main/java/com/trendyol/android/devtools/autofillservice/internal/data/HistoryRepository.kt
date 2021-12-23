package com.trendyol.android.devtools.autofillservice.internal.data

import com.trendyol.android.devtools.autofillservice.internal.model.ListItem

internal interface HistoryRepository {

    suspend fun save(fields: List<String>, item: ListItem.Autofill)

    suspend fun getLast(fields: List<String>): ListItem.Autofill?
}
