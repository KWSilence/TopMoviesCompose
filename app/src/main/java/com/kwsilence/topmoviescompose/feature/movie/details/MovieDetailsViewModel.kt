package com.kwsilence.topmoviescompose.feature.movie.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.usecase.movie.GetMovieDetailsUseCase
import com.kwsilence.topmoviescompose.util.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {
    var state: MovieDetailsScreenState by mutableStateOf(MovieDetailsScreenState())
        private set

    private companion object {
        const val DEFAULT_IMAGE_WIDTH = 400
    }

    fun getFlowMovieDetails(id: Int) = getMovieDetailsUseCase.getFlow(movieId = id)

    fun getMovieDetails(id: Int, force: Boolean = false) {
        if (state.isRefreshing || !force && (state.details != null || state.error != null)) return
        val onFailure: (Throwable) -> Unit = { error ->
            state = state.copy(isRefreshing = false, error = error.localizedMessage.toEvent())
            Timber.e(error)
        }
        viewModelScope.launch {
            runCatching {
                state = state.copy(isRefreshing = true)
                withContext(Dispatchers.IO) {
                    getMovieDetailsUseCase(
                        movieId = id,
                        imageWidth = DEFAULT_IMAGE_WIDTH,
                        loadMode = if (force) LoadMode.FORCE else LoadMode.DEFAULT
                    )
                }.onSuccess { details ->
                    state = state.copy(details = details, isRefreshing = false)
                }.onFailure { error ->
                    onFailure(error)
                }
            }.onFailure { error ->
                onFailure(error)
            }
        }
    }
}
