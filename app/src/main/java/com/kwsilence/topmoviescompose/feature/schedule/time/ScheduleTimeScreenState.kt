package com.kwsilence.topmoviescompose.feature.schedule.time

import android.icu.util.Calendar
import com.kwsilence.topmoviescompose.domain.model.MovieDetails
import com.kwsilence.topmoviescompose.util.Event
import com.kwsilence.topmoviescompose.util.toEvent
import java.util.Date

data class ScheduleTimeScreenState(
    val dateTime: Date? = null,
    val movie: MovieDetails? = null,
    val scheduleCompleted: Event<Boolean> = false.toEvent(),
    val deleteCompleted: Event<Boolean> = false.toEvent(),
    val error: Event<String>? = null
) {
    private val calendar: Calendar = Calendar.getInstance().apply { dateTime?.let { time = it } }

    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    val minute: Int = calendar.get(Calendar.MINUTE)
}
