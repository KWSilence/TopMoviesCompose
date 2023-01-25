package com.kwsilence.topmoviescompose.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.kwsilence.topmoviescompose.MainActivity
import com.kwsilence.topmoviescompose.R

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        private const val CHANNEL_ID = "ScheduledMovieChannel"
        private const val CHANNEL_NAME = "ScheduledMovie"
        private const val CHANNEL_DESCRIPTION = "Notifications for scheduled movies"

        const val ID_FIELD = "id"
        const val TITLE_FIELD = "title"
        const val TEXT_FIELD = "text"
        const val URI_STRING_FIELD = "uri_string"
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
        val contentIntent = intent.getStringExtra(URI_STRING_FIELD)?.let { uriString ->
            PendingIntent.getActivity(
                context,
                0,
                Intent(
                    Intent.ACTION_VIEW,
                    uriString.toUri(),
                    context,
                    MainActivity::class.java
                ),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setAutoCancel(true)
            setSmallIcon(R.drawable.ic_movies)
            setContentTitle(intent.getStringExtra(TITLE_FIELD))
            setContentText(intent.getStringExtra(TEXT_FIELD))
            setContentIntent(contentIntent) // todo intent
            setChannelId(CHANNEL_ID)
        }.build()
        notificationManager.notify(intent.getIntExtra(ID_FIELD, 0), notification)
    }
}
