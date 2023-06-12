package com.kwsilence.topmoviescompose.feature.movie.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.MoviesPage
import com.kwsilence.topmoviescompose.domain.usecase.movie.GetPopularMovieListUseCase
import com.kwsilence.topmoviescompose.exception.toTopMoviesError
import com.kwsilence.topmoviescompose.util.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModel(
    private val getPopularMovieListUseCase: GetPopularMovieListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MovieListScreenState())
    private val state get() = _state.value
    private val _movieList = _state.flatMapLatest {
        getPopularMovieListUseCase.getFlow(it.currentPage)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val movieListState = combine(_state, _movieList) { state, movieList ->
        if (state.movieList.size <= movieList.size && state.movieList != movieList) {
            _state.update { it.copy(movieList = movieList) }
        }
        state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), MovieListScreenState())

    private companion object {
        const val DEFAULT_IMAGE_WIDTH = 400
        const val RETRY_REQUEST_MS_DELAY_AFTER_ERROR = 3000L
    }

    private var lastRequestTime: Date? = null

    init {
        refresh(force = false)
    }

    fun loadMoreMovies(retry: Boolean = false) {
        val nextPage = state.currentPage + 1
        val canRetry = retry || !state.showRetry
        if (!state.canLoadMore || !canRetry || state.isLoading || state.isRefreshing) return
        getMovieList(
            onStart = { _state.update { it.copy(isLoading = true) } },
            nextPage = nextPage,
            onSuccess = { moviesPage ->
                _state.update {
                    it.copy(
                        movieList = state.movieList + moviesPage.movieList,
                        canLoadMore = moviesPage.canLoadMore,
                        showRetry = false,
                        isLoading = false,
                        currentPage = nextPage
                    )
                }
            },
            retry = retry,
            onFailure = { error ->
                _state.update {
                    it.copy(isLoading = false, error = error.toTopMoviesError().toEvent())
                }
            }
        )
    }

    fun refresh(force: Boolean = true) {
        if (state.isRefreshing || state.isLoading) return
        val initialPage = 1
        getMovieList(
            onStart = { _state.update { it.copy(isRefreshing = true) } },
            nextPage = initialPage,
            onSuccess = { moviesPage ->
                _state.update {
                    it.copy(
                        movieList = moviesPage.movieList,
                        canLoadMore = moviesPage.canLoadMore,
                        showRetry = false,
                        isRefreshing = false,
                        currentPage = initialPage
                    )
                }
            },
            onFailure = { error ->
                _state.update {
                    it.copy(isRefreshing = false, error = error.toTopMoviesError().toEvent())
                }
            },
            force = force
        )
    }

    private fun getMovieList(
        onStart: () -> Unit,
        nextPage: Int,
        onSuccess: (MoviesPage) -> Unit,
        onFailure: (Throwable) -> Unit,
        onRequestDelay: () -> Unit = { _state.update { it.copy(showRetry = it.canLoadMore) } },
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
