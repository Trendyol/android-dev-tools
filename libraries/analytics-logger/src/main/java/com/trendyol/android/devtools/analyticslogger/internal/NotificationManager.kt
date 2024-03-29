package com.trendyol.android.devtools.analyticslogger.internal

import android.Manifest
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
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.di.AnalyticsContainer
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class NotificationManager constructor(private var showNotification: Boolean) {

    private val superVisorJob = SupervisorJob()
    private val scope = CoroutineScope(superVisorJob + Dispatchers.IO)
    private val analyticsContainer: AnalyticsContainer by lazy { ContextContainer.analyticsContainer }
    private val intent by lazy {
        Intent(ContextContainer.getContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
    private var lastEvent: Pair<String?, String?>? = null

    init {
        initNotificationChannel()
    }

    internal fun show() {
        ContextContainer.getContext().startActivity(intent)
    }

    internal fun showNotification() {
        showNotification = true
        lastEvent?.let { (key, platform) ->
            updateNotification(key, platform)
        }
    }

    internal fun hideNotification() {
        showNotification = false
        cancelNotification()
    }

    internal fun cancelNotification() {
        NotificationManagerCompat.from(ContextContainer.getContext()).cancel(NOTIFICATION_ID)
    }

    internal fun reportEvent(
        key: String?,
        value: String?,
        platform: String?,
    ) = scope.launch {
        analyticsContainer.eventManager.insert(
            key = key,
            value = value,
            platform = platform,
        )
        lastEvent = key to platform
        updateNotification(
            key = key,
            platform = platform,
        )
    }

    private fun updateNotification(key: String?, platform: String?) {
        if (showNotification.not()) return
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(ContextContainer.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(ContextContainer.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val content = ContextContainer.getContext().getString(
            R.string.analytics_logger_notification_content,
            platform,
            key,
        )
        val builder = NotificationCompat.Builder(
            ContextContainer.getContext(),
            ContextContainer.getContext().getString(R.string.analytics_logger_notification_channel_id),
        )
            .setSmallIcon(R.drawable.analytics_logger_insights)
            .setContentTitle(ContextContainer.getContext().getString(R.string.analytics_logger_notification_title))
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)

        if (ActivityCompat.checkSelfPermission(
                ContextContainer.getContext(),
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(ContextContainer.getContext()).notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ContextContainer.getContext().getString(R.string.analytics_logger_notification_channel_id),
                ContextContainer.getContext().getString(R.string.analytics_logger_notification_channel_name),
                NotificationManager.IMPORTANCE_LOW,
            )
            val notificationManager: NotificationManager =
                ContextContainer.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {

        private const val NOTIFICATION_ID = 2625
    }
}
