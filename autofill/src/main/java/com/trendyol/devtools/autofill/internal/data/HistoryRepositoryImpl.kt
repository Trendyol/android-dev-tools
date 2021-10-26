package com.trendyol.devtools.autofill.internal.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.Moshi
import com.trendyol.devtools.autofill.internal.model.ListItemEntity
import kotlinx.coroutines.flow.first

internal class HistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    moshi: Moshi,
) : HistoryRepository {

    private val adapter = moshi.adapter(ListItemEntity::class.java)

    override suspend fun save(entity: ListItemEntity) {
        dataStore.edit { settings ->
            settings[KEY_HISTORY] = runCatching { adapter.toJson(entity) }.getOrNull().orEmpty()
        }
    }

    override suspend fun getLast(): ListItemEntity? {
        return runCatching {
            adapter.fromJson(dataStore.data.first()[KEY_HISTORY].orEmpty())
        }.getOrNull()
    }

    companion object {
        private val KEY_HISTORY = stringPreferencesKey("history")
    }
}
