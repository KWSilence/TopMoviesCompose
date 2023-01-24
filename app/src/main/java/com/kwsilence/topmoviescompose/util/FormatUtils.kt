package com.kwsilence.topmoviescompose.util

import com.kwsilence.topmoviescompose.domain.util.LanguageUtils
import java.text.SimpleDateFormat
import java.util.Date

object FormatUtils {
    private val locale get() = LanguageUtils.locale
    private val releaseDateFormat
        get() = SimpleDateFormat("MMMM d, yyyy", locale)
    private val scheduleDateFormat
        get() = SimpleDateFormat("dd.MM.yyyy HH:mm", locale)
    private val scheduleDayDateFormat
        get() = SimpleDateFormat("dd MMMM yyyy", locale)
    private val scheduleTimeDateFormat
        get() = SimpleDateFormat("HH:mm", locale)
    private const val moneyFormat = "%,d \$"

    fun formatMovieReleaseDate(date: Date): String = releaseDateFormat.format(date)
    fun formatScheduleDate(date: Date): String = scheduleDateFormat.format(date)
    fun formatScheduleDay(date: Date): String = scheduleDayDateFormat.format(date)
    fun formatScheduleTime(date: Date): String = scheduleTimeDateFormat.format(date)
    fun formatMoney(money: Long): String = moneyFormat.format(money)
}
