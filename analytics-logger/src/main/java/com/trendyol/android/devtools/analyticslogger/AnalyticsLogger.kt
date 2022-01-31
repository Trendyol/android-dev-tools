package com.trendyol.android.devtools.analyticslogger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.trendyol.android.devtools.analyticslogger.api.platform.EventPlatform
import com.trendyol.android.devtools.analyticslogger.internal.di.AnalyticsContainer
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainActivity
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

    private val analyticsContainer: AnalyticsContainer by lazy { ContextContainer.analyticsContainer }

    private fun reportEvent(
        key: String?,
        value: String?,
        platform: EventPlatform?,
    ) = scope.launch {
        analyticsContainer.eventManager.insert(
            key = key,
            value = value,
            platform = platform,
        )
        updateNotification(
            key = key,
            value = value,
        )
    }

    private fun updateNotification(
        key: String?,
        value: String?,
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, "ch")
            .setSmallIcon(R.drawable.ic_insights_black_24dp)
            .setContentTitle("Analytics Logger")
            .setContentText("sa:  $key -> $value")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(2, builder.build())
        }
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
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
            ContextContainer.initialize(application)
        }

        fun report(
            key: String,
            value: String,
            platform: EventPlatform,
        ) {
            instance?.reportEvent(
                key = key,
                value = value,
                platform = platform,
            )
        }
    }
}
