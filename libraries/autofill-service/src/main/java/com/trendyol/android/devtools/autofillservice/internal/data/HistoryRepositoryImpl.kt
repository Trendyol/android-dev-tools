package com.trendyol.android.devtools.autofillservice.internal.data

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.trendyol.android.devtools.autofillservice.internal.model.ListItem

internal class HistoryRepositoryImpl(
    private val preferences: SharedPreferences,
    moshi: Moshi,
) : HistoryRepository {

    private val adapter = moshi.adapter(ListItem.Autofill::class.java)

    override suspend fun save(fields: List<String>, item: ListItem.Autofill) {
        val data = runCatching { adapter.toJson(item) }.getOrNull().orEmpty()
        preferences.edit().putString(getDataKey(fields), data).apply()
    }

    override suspend fun getLast(fields: List<String>): ListItem.Autofill? {
        return runCatching {
            adapter.fromJson(
                preferences.getString(getDataKey(fields), null).orEmpty()
            )
        }.getOrNull()
    }

    private fun getDataKey(fields: List<String>): String {
        return KEY_HISTORY + fields.joinToString(separator = "")
    }

    companion object {
        private const val KEY_HISTORY = "history"
    }
}
