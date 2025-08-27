package com.trendyol.android.devtools.analyticslogger.internal.domain.usecase

import android.util.Log
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.ExcludeKeysRepository
import org.json.JSONException
import org.json.JSONObject

internal class ExcludeKeysUseCase(
    private val excludeKeysRepository: ExcludeKeysRepository
) {

    fun saveExcludedKeys(keys: String) {
        excludeKeysRepository.saveExcludedKeys(keys)
    }

    fun getExcludedKeys(): String {
        return excludeKeysRepository.getExcludedKeys()
    }

    fun getExcludedKeysList(): List<String> {
        return getExcludedKeys()
            .split(" ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    fun filterJsonByExcludedKeys(jsonString: String, excludedKeys: List<String>): String {
        if (excludedKeys.isEmpty() || jsonString.isEmpty()) {
            return jsonString
        }

        return try {
            val jsonObject = JSONObject(jsonString)
            excludedKeys.forEach { key ->
                removeKeyRecursively(jsonObject, key)
            }
            jsonObject.toString(4)
        } catch (e: JSONException) {
            Log.e("AnalyticsLogger", e.message.toString())
            jsonString
        }
    }

    private fun removeKeyRecursively(jsonObject: JSONObject, keyToRemove: String) {
        jsonObject.remove(keyToRemove)

        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = jsonObject.opt(key)
            if (value is JSONObject) {
                removeKeyRecursively(value, keyToRemove)
            }
        }
    }
}
