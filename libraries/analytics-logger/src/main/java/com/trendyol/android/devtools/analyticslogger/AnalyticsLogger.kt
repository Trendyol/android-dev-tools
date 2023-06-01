package com.trendyol.android.devtools.analyticslogger

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
        platform: String?,
    ) = scope.launch {
        analyticsContainer.eventManager.insert(
            key = key,
            value = value,
            platform = platform,
        )
        updateNotification(
            key = key,
            platform = platform,
        )
    }

    private fun updateNotification(key: String?, platform: String?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val content = context.getString(
            R.string.analytics_logger_notification_content,
            platform,
            key,
        )
        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.analytics_logger_notification_channel_id),
        )
            .setSmallIcon(R.drawable.analytics_logger_insights)
            .setContentTitle(context.getString(R.string.analytics_logger_notification_title))
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, builder.build())
            }
        }
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.getString(R.string.analytics_logger_notification_channel_id),
                context.getString(R.string.analytics_logger_notification_channel_name),
                NotificationManager.IMPORTANCE_LOW,
            )
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
            key: String?,
            value: String?,
            platform: String?,
        ) {
            instance?.reportEvent(
                key = key,
                value = value,
                platform = platform,
            )
        }
    }
}
