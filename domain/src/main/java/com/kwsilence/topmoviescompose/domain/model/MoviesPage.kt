package com.kwsilence.topmoviescompose.domain.model

data class MoviesPage(
    val movieList: List<Movie>,
    val canLoadMore: Boolean
)
