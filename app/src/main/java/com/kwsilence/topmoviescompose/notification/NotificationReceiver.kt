package com.kwsilence.topmoviescompose.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kwsilence.topmoviescompose.R

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        private const val CHANNEL_ID = "ScheduledMovieChannel"
        private const val CHANNEL_NAME = "ScheduledMovie"
        private const val CHANNEL_DESCRIPTION = "Notifications for scheduled movies"

        const val ID_FIELD = "id"
        const val TITLE_FIELD = "title"
        const val TEXT_FIELD = "text"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setAutoCancel(true)
            setSmallIcon(R.drawable.ic_movies)
            setContentTitle(intent.getStringExtra(TITLE_FIELD))
            setContentText(intent.getStringExtra(TEXT_FIELD))
            setChannelId(CHANNEL_ID)
        }.build()
        notificationManager.notify(intent.getIntExtra(ID_FIELD, 0), notification)
    }
}
