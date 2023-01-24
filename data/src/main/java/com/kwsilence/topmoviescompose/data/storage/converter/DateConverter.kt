package com.kwsilence.topmoviescompose.data.storage.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverter {
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US)

    @TypeConverter
    fun dateFromString(dateString: String?): Date? = dateString?.let { dateFormat.parse(it) }

    @TypeConverter
    fun stringFromDate(date: Date?): String? = date?.let { dateFormat.format(it) }
}
