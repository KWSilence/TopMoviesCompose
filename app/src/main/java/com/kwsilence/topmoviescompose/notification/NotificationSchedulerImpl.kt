package com.kwsilence.topmoviescompose.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kwsilence.topmoviescompose.domain.notification.NotificationScheduler
import java.util.Date

class NotificationSchedulerImpl(private val context: Context) : NotificationScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(id: Int, date: Date, title: String, text: String) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.TITLE_FIELD, title)
            putExtra(NotificationReceiver.TEXT_FIELD, text)
            putExtra(NotificationReceiver.ID_FIELD, id)
        }
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, flags)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.time, pendingIntent)
    }

    override fun cancel(id: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, flags)
        alarmManager.cancel(pendingIntent)
    }
}
