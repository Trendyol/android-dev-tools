package com.trendyol.android.devtools.autofill.internal.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.autofill.internal.model.ListItem
import kotlinx.coroutines.flow.first

internal class HistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    moshi: Moshi,
) : HistoryRepository {

    private val adapter = moshi.adapter(ListItem.Autofill::class.java)

    override suspend fun save(fields: List<String>, item: ListItem.Autofill) {
        dataStore.edit { settings ->
            settings[getDataKey(fields)] = runCatching { adapter.toJson(item) }.getOrNull().orEmpty()
        }
    }

    override suspend fun getLast(fields: List<String>): ListItem.Autofill? {
        return runCatching {
            adapter.fromJson(dataStore.data.first()[getDataKey(fields)].orEmpty())
        }.getOrNull()
    }

    private fun getDataKey(fields: List<String>): Preferences.Key<String> {
        return stringPreferencesKey(KEY_HISTORY + fields.joinToString(separator = ""))
    }

    companion object {
        private const val KEY_HISTORY = "history"
    }
}
