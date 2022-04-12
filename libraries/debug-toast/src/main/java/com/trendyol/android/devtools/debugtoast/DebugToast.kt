package com.trendyol.android.devtools.debugtoast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class DebugToast private constructor() {

    private var isActive = false

    fun show(context: Context, text: String, duration: Int) {
        Toast.makeText(context, text, duration).show()
    }

    private fun updateNotification(context: Context) {
        val actionIntent = createActionIntent(context)

        val actionText = context.getString(
            if (isActive) {
                R.string.debug_toast_notification_action_stop
            } else {
                R.string.debug_toast_notification_action_start
            }
        )

        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.debug_toast_notification_channel_id),
        )
            .setSmallIcon(R.drawable.ic_dev_tools_debug_toast_bug)
            .setContentTitle(context.getString(R.string.debug_toast_notification_title))
            .setContentText(context.getString(R.string.debug_toast_notification_content))
            .addAction(R.drawable.ic_dev_tools_debug_toast_bug, actionText, actionIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createActionIntent(context: Context): PendingIntent {
        val actionIntent = Intent(INTENT_ACTION_TOGGLE_TOAST)
        return PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            actionIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            },
        )
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            isActive = !isActive
            context?.let { updateNotification(it) }
        }
    }

    private fun initBroadcastReceiver(context: Context) {
        context.registerReceiver(
            broadcastReceiver,
            IntentFilter(INTENT_ACTION_TOGGLE_TOAST),
        )
    }

    private fun initNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channelId = context.getString(R.string.debug_toast_notification_channel_id)

        val channel = NotificationChannel(
            channelId,
            context.getString(R.string.debug_toast_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW,
        )
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    companion object {

        private const val INTENT_ACTION_TOGGLE_TOAST = "com.trendyol.android.devtools.action.TOGGLE_TOAST"
        private const val NOTIFICATION_ID = 1881

        private var INSTANCE: DebugToast? = null

        fun init(context: Context) {
            if (INSTANCE == null) INSTANCE = DebugToast()
            INSTANCE?.initNotificationChannel(context)
            INSTANCE?.initBroadcastReceiver(context)
        }

        fun show(context: Context, text: String, duration: Int) {
            INSTANCE?.updateNotification(context)

            if (INSTANCE?.isActive == true) {
                INSTANCE?.show(context, text, duration)
            }
        }
    }
}
