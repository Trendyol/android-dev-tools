package com.trendyol.devtools.autofill.internal.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.Moshi
import com.trendyol.devtools.autofill.internal.model.AutofillEntity
import com.trendyol.devtools.autofill.internal.model.AutofillWrapperEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    moshi: Moshi,
) : HistoryRepository {

    private val adapter = moshi.adapter(AutofillWrapperEntity::class.java)

    override suspend fun save(entity: AutofillEntity) {
        dataStore.edit { settings ->
            val currentData = runCatching { adapter.fromJson(settings[KEY_HISTORY].orEmpty()) }
                .getOrNull()
                ?.data
                .orEmpty()
                .toMutableList()

            currentData.add(entity)

            val newData = runCatching { adapter.toJson(AutofillWrapperEntity(currentData)) }.getOrNull()
            settings[KEY_HISTORY] = newData.orEmpty()
        }
    }

    override suspend fun getLast(): AutofillEntity {
        return getAll().last()
    }

    override suspend fun getAll(): List<AutofillEntity> {
        return adapter.fromJson(dataStore.data.first()[KEY_HISTORY])?.data.orEmpty()
    }

    companion object {
        private val KEY_HISTORY = stringPreferencesKey("test")
    }
}
