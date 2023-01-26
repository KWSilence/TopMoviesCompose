package com.kwsilence.topmoviescompose.feature.movie.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.model.MoviesPage
import com.kwsilence.topmoviescompose.domain.usecase.movie.GetPopularMovieListUseCase
import com.kwsilence.topmoviescompose.exception.toTopMoviesError
import com.kwsilence.topmoviescompose.util.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

class MovieListViewModel(
    private val getPopularMovieListUseCase: GetPopularMovieListUseCase
) : ViewModel() {
    var state: MovieListScreenState by mutableStateOf(MovieListScreenState())
        private set

    private companion object {
        const val DEFAULT_IMAGE_WIDTH = 400
        const val RETRY_REQUEST_MS_DELAY_AFTER_ERROR = 3000L
    }

    private var lastRequestTime: Date? = null

    init {
        refresh(force = false)
    }

    fun getFlowMovieList(page: Int) = getPopularMovieListUseCase.getFlow(page)

    fun saveMovieListChanges(movieList: List<Movie>) {
        viewModelScope.launch {
            val updatedMovieList = state.movieList.map { movie ->
                movieList.find { newMovie -> newMovie.id == movie.id } ?: movie
            }
            state = state.copy(movieList = updatedMovieList)
        }
    }

    fun loadMoreMovies(retry: Boolean = false) {
        val canRetry = retry || !state.showRetry
        if (!state.canLoadMore || !canRetry || state.isLoading || state.isRefreshing) return
        val nextPage = state.currentPage + 1
        getMovieList(
            onStart = { state = state.copy(isLoading = true, showRetry = false) },
            nextPage = nextPage,
            onSuccess = { moviesPage ->
                state = state.copy(
                    movieList = state.movieList + moviesPage.movieList,
                    canLoadMore = moviesPage.canLoadMore,
                    showRetry = false,
                    isLoading = false,
                    currentPage = nextPage
                )
            },
            retry = retry,
            onFailure = { error ->
                state = state.copy(isLoading = false, error = error.toTopMoviesError().toEvent())
            }
        )
    }

    fun refresh(force: Boolean = true) {
        if (state.isRefreshing || state.isLoading) return
        val initialPage = 1
        getMovieList(
            onStart = { state = state.copy(isRefreshing = true) },
            nextPage = initialPage,
            onSuccess = { moviesPage ->
                state = state.copy(
                    movieList = moviesPage.movieList,
                    canLoadMore = moviesPage.canLoadMore,
                    showRetry = false,
                    isRefreshing = false,
                    currentPage = initialPage
                )
            },
            onFailure = { error ->
                state = state.copy(
                    isRefreshing = false, error = error.toTopMoviesError().toEvent()
                )
            },
            force = force
        )
    }

    private fun getMovieList(
        onStart: () -> Unit,
        nextPage: Int,
        onSuccess: (MoviesPage) -> Unit,
        onFailure: (Throwable) -> Unit,
        onRequestDelay: () -> Unit = { state = state.copy(showRetry = state.canLoadMore) },
        retry: Boolean = false,
        force: Boolean = false
    ) {
        val lrTime = lastRequestTime?.time
        val currentTime = Date().time
        if (!retry && lrTime != null && lrTime + RETRY_REQUEST_MS_DELAY_AFTER_ERROR > currentTime) {
            onRequestDelay()
            return
        }

        viewModelScope.launch {
            runCatching {
                onStart()
                withContext(Dispatchers.IO) {
                    getPopularMovieListUseCase(
                        page = nextPage,
                        imageWidth = DEFAULT_IMAGE_WIDTH,
                        loadMode = if (force) LoadMode.FORCE else LoadMode.DEFAULT
                    )
                }.onSuccess { moviesPage ->
                    onSuccess(moviesPage)
                }.onFailure { error ->
                    lastRequestTime = Date()
                    onFailure(error)
                    Timber.e(error)
                }
            }.onFailure { error ->
                lastRequestTime = Date()
                onFailure(error)
                Timber.e(error)
            }
        }
    }
}
