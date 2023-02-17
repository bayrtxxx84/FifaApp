package com.example.test.ui.extras

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ComponentActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.test.R
import com.example.test.ui.activities.PrincipalActivity
import kotlinx.coroutines.CoroutineScope
import kotlin.random.Random


enum class EnumChannels() {
    CHAT {
        override fun getValues(): String = "Chat"
    },
    BUSSINESS {
        override fun getValues(): String = "Negocios"
    };

    abstract fun getValues(): String
}

object ManageNotifications {

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel1 =
                NotificationChannel(
                    EnumChannels.CHAT.getValues(),
                    EnumChannels.CHAT.getValues(),
                    importance
                )
            val channel2 = NotificationChannel(
                EnumChannels.BUSSINESS.getValues(),
                EnumChannels.BUSSINESS.getValues(),
                importance
            )
            // Register the channel with the system
            val notificationManager =
                context.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }


    @SuppressLint("MissingPermission")
    fun sendNotification(
        context: Context,
        CHANNEL_ID: String,
        title: String,
        message: String
    ) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, PrincipalActivity::class.java).apply {
            putExtra("saludo", "FINALIZAMOS")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active_24)
            .setContentTitle(title)
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            val notificationId = Random.nextInt(0, 1000000)
            notify(notificationId, builder.build())
        }
    }
}