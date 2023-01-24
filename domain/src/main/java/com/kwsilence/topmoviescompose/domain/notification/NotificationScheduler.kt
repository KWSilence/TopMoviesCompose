package com.kwsilence.topmoviescompose.domain.notification

import java.util.Date

interface NotificationScheduler {
    fun schedule(id: Int, date: Date, title: String, text: String)
    fun cancel(id: Int)
}
