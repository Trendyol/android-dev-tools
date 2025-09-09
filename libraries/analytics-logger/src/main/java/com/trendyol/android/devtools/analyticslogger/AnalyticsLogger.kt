package com.trendyol.android.devtools.analyticslogger

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import com.trendyol.android.devtools.analyticslogger.internal.NotificationManager
import com.trendyol.android.devtools.analyticslogger.internal.di.analyticsLoggerModule
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.android.ext.koin.androidLogger
import embedded.koin.core.Koin
import embedded.koin.core.context.GlobalContext.startKoin
import embedded.koin.core.logger.Level
import embedded.koin.dsl.koinApplication
import kotlin.getValue

object AnalyticsLogger {

    internal lateinit var koin: Koin

    private val instance: NotificationManager by lazy { koin.get() }
    private val sharedPreferences: SharedPreferences by lazy { koin.get() }

    fun init(application: Application, showNotification: Boolean = true) {
        koin = koinApplication {
            androidContext(application.applicationContext)
            androidLogger(Level.DEBUG)

            modules(
                analyticsLoggerModule(
                    application = application,
                    showNotification = showNotification,
                )
            )
        }.koin
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
        sharedPreferences
            .edit {
                putString("jsTransformFunction", jsFunction)
            }
    }

    fun getEventTransformFunction(): String {
        return sharedPreferences
            .getString("jsTransformFunction", "")
            .orEmpty()
    }

    fun show() {
        instance.show()
    }

    fun showNotification() {
        instance.showNotification()
    }

    fun hideNotification() {
        instance.hideNotification()
    }

    fun report(key: String?, value: String?, platform: String?, source: String?) {
        instance.reportEvent(key, value, platform, source)
    }

    /**
     * Reports an analytics event with comprehensive tracking information.
     *
     * @param key The key identifying the event. Can be null.
     * @param value The value associated with the event (usually JSON data). Can be null.
     * @param platform The analytics platform (e.g., "Firebase", "Amplitude"). Can be null.
     * @param isSuccess Indicates whether the operation was successful. Can be null.
     * @param source The source of the event (e.g., "com.trendyol.product.ProductClickEvent"). Can be null.
     */
    fun report(
        key: String?,
        value: String?,
        platform: String?,
        source: String?,
        isSuccess: Boolean? = null,
    ) {
        instance.reportEvent(key, value, platform, source, isSuccess)
    }
}
