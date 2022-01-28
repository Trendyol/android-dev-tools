package com.trendyol.android.devtools.analyticslogger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.trendyol.android.devtools.analyticslogger.internal.data.database.EventDatabase
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepository
import com.trendyol.android.devtools.analyticslogger.internal.data.repository.EventRepositoryImpl
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManager
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManagerImpl
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AnalyticsLogger private constructor(
    private val application: Application,
) {

    private val context get() = application.applicationContext

    private val superVisorJob = SupervisorJob()

    private val scope = CoroutineScope(superVisorJob + Dispatchers.IO)

    private val eventDatabase: EventDatabase by lazy { EventDatabase.create(context) }

    private val eventRepository: EventRepository by lazy { EventRepositoryImpl(eventDatabase) }

    private val eventManager: EventManager by lazy { EventManagerImpl(eventRepository) }

    private fun reportEvent(event: Event) = scope.launch {
        eventManager.insert(event)
        updateNotification(event)
    }

    private fun updateNotification(event: Event) {
        val builder = NotificationCompat.Builder(context, "ch")
            .setSmallIcon(R.drawable.ic_insights_black_24dp)
            .setContentTitle("Analytics Logger")
            .setContentText("sa: ${event.uid} ${event.key} -> ${event.value}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(2, builder.build())
        }
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("ch", "test", importance).apply {
                description = "test"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {

        private var instance: AnalyticsLogger? = null

        fun init(application: Application) {
            instance = AnalyticsLogger(application)
            instance?.initNotificationChannel()
        }

        fun report(
            key: String,
            value: String,
        ) {
            val event = Event(
                key = key,
                value = value,
            )
            instance?.reportEvent(event)
        }
    }
}
