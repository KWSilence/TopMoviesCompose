package com.kwsilence.topmoviescompose.feature.schedule.time

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.toMovie
import com.kwsilence.topmoviescompose.domain.usecase.movie.GetMovieDetailsUseCase
import com.kwsilence.topmoviescompose.domain.usecase.schedule.DeleteScheduleUseCase
import com.kwsilence.topmoviescompose.domain.usecase.schedule.ScheduleMovieWatchingUseCase
import com.kwsilence.topmoviescompose.navigation.ScreenDeepLink
import com.kwsilence.topmoviescompose.util.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

class ScheduleTimeViewModel(
    private val scheduleMovieWatchingUseCase: ScheduleMovieWatchingUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase
) : ViewModel() {
    var state by mutableStateOf(ScheduleTimeScreenState())
        private set

    private val calendar get() = Calendar.getInstance().apply { state.dateTime?.let { time = it } }

    private fun getDateWithOffset(hourOffset: Int = 1): Date = Calendar.getInstance().apply {
        add(Calendar.HOUR_OF_DAY, hourOffset)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    fun loadMovieInfo(movieId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getMovieDetailsUseCase(movieId, loadMode = LoadMode.LOCAL)
            }.onSuccess { movie ->
                state = state.copy(movie = movie, dateTime = movie.scheduled ?: getDateWithOffset())
            }.onFailure { error ->
                state = state.copy(error = error.localizedMessage.toEvent())
                Timber.e(error)
            }
        }
    }

    fun setDate(year: Int, month: Int, day: Int) {
        state = calendar.run {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            state.copy(dateTime = time)
        }
    }

    fun setTime(hour: Int, minute: Int) {
        state = calendar.run {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            state.copy(dateTime = time)
        }
    }

    fun submitTime() {
        val dateTime = state.dateTime ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    val movie = state.movie?.toMovie()
                    val uriString = movie?.id?.let { id ->
                        ScreenDeepLink.MovieDetails.buildUriString(movieId = id)
                    }
                    scheduleMovieWatchingUseCase(
                        movie = movie,
                        time = dateTime,
                        uriString = uriString
                    )
                }
            }.onSuccess {
                state = state.copy(scheduleCompleted = true.toEvent())
            }.onFailure { error ->
                state = state.copy(
                    scheduleCompleted = false.toEvent(),
                    error = error.localizedMessage.toEvent()
                )
                Timber.e(error)
            }
        }
    }

    fun deleteSchedule() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    deleteScheduleUseCase(state.movie?.id)
                }
            }.onSuccess {
                state = state.copy(deleteCompleted = true.toEvent())
            }.onFailure { error ->
                state = state.copy(
                    deleteCompleted = false.toEvent(),
                    error = error.localizedMessage.toEvent()
                )
                Timber.e(error)
            }
        }
    }
}
