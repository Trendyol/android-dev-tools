package com.trendyol.android.devtools.analyticslogger

import android.app.Application
import android.util.Log
import androidx.core.content.edit
import com.trendyol.android.devtools.analyticslogger.internal.NotificationManager
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer

object AnalyticsLogger {

    internal var instance: NotificationManager? = null
    private const val TAG = "AnalyticsLogger"
    private const val INIT_ERROR_MESSAGE = "Should call AnalyticsLogger.init(Application, Boolean) first."

    fun init(application: Application, showNotification: Boolean = true) {
        ContextContainer.initialize(application)
        instance = NotificationManager(showNotification)
    }

    /**
     * Sets a JavaScript function to transform event data.
     * The function should be named 'transform' and take a single string parameter.
     *
     * Example:
     * ```
     * AnalyticsLogger.setEventTransformFunction("""
     *     function transform(data) {
     *         var parsed = JSON.parse(data);
     *         return 'TRANSFORMED: ' + parsed.eventKey;
     *     }
     * """)
     * ```
     */
    fun setEventTransformFunction(jsFunction: String) {
        ContextContainer
            .analyticsContainer
            .sharedPreferencesManager
            .edit {
                putString("jsTransformFunction", jsFunction)
            }
    }

    fun getEventTransformFunction(): String {
        return ContextContainer
            .analyticsContainer
            .sharedPreferencesManager
            .getString("jsTransformFunction", "")
            .orEmpty()
    }

    fun show() {
        instance?.show() ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }

    fun showNotification() {
        instance?.showNotification() ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }

    fun hideNotification() {
        instance?.hideNotification() ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }

    fun report(key: String?, value: String?, platform: String?) {
        instance?.reportEvent(key, value, platform) ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }

    /**
     * Reports an event with the specified key, value, platform, and success status.
     *
     * @param key The key identifying the event. Can be null.
     * @param value The value associated with the event. Can be null.
     * @param platform The platform related to the event. Can be null.
     * @param isSuccess Indicates whether the operation was successful. Can be null.
     */
    fun report(
        key: String?,
        value: String?,
        platform: String?,
        isSuccess: Boolean?,
    ) {
        instance?.reportEvent(key, value, platform, isSuccess) ?: Log.w(TAG, INIT_ERROR_MESSAGE)
    }
}
