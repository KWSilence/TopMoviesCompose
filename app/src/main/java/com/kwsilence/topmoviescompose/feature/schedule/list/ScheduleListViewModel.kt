package com.kwsilence.topmoviescompose.feature.schedule.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.usecase.schedule.DeleteScheduleUseCase
import com.kwsilence.topmoviescompose.domain.usecase.schedule.GetScheduledMovieListUseCase
import com.kwsilence.topmoviescompose.util.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ScheduleListViewModel(
    getScheduledMovieListUseCase: GetScheduledMovieListUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase
) : ViewModel() {
    var state by mutableStateOf(ScheduleListScreenState())
        private set
    val scheduledMovieListFlow = getScheduledMovieListUseCase.getFlow()

    fun saveMovieListChanges(movieList: List<Movie>) {
        viewModelScope.launch {
            state = state.copy(movieList = movieList)
        }
    }

    fun deleteSchedule(movie: Movie) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    deleteScheduleUseCase.invoke(movie.id)
                }
            }.onSuccess {
                state = state.copy(deletedMovieSchedule = movie.toEvent())
            }.onFailure { error ->
                state = state.copy(error = error.localizedMessage.toEvent())
                Timber.e(error)
            }
        }
    }
}
