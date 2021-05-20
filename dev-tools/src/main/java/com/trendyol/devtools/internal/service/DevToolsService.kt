package com.trendyol.devtools.internal.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.trendyol.devtools.R
import com.trendyol.devtools.internal.ui.MainActivity

internal class DevToolsService : Service() {

    override fun onCreate() {
        super.onCreate()

        showNotification()
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException(intent.toString())
    }

    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        startForeground(ONGOING_NOTIFICATION_ID, createNotification())
    }

    private fun createNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setOngoing(true)
            .setAutoCancel(false)
            .setColor(ContextCompat.getColor(this, R.color.dev_tools_black))
            .setSmallIcon(R.drawable.dev_tools_notification_badge)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(getContentPendingIntent())
            .setContentText(getString(R.string.dev_tools_notification_description))
            .build()

    private fun getContentPendingIntent(): PendingIntent =
        PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.dev_tools_notification_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(notificationChannel)
    }

    companion object {

        private const val CHANNEL_ID = "com.trendyol.devtools"
        private const val ONGOING_NOTIFICATION_ID = 1881

        internal fun initializeService(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, DevToolsService::class.java))
            } else {
                context.startService(Intent(context, DevToolsService::class.java))
            }
        }
    }
}
