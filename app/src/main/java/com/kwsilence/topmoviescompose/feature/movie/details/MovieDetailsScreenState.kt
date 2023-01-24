package com.kwsilence.topmoviescompose.feature.movie.details

import com.kwsilence.topmoviescompose.domain.model.MovieDetails
import com.kwsilence.topmoviescompose.util.Event

data class MovieDetailsScreenState(
    val details: MovieDetails? = null,
    val isRefreshing: Boolean = false,
    val error: Event<String>? = null
)
