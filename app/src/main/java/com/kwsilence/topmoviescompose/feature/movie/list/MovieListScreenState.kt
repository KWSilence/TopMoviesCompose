package com.kwsilence.topmoviescompose.feature.movie.list

import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.util.Event

data class MovieListScreenState(
    val movieList: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val showRetry: Boolean = false,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true,
    val error: Event<String>? = null
)
