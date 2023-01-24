package com.kwsilence.topmoviescompose.domain.model

import java.util.Date

data class Movie(
    val adult: Boolean,
    val backdropPath: String?,
    val id: Int,
    val originalLang: String,
    val originalTitle: String,
    val overview: String?,
    val popularity: Float,
    val posterPath: String?,
    val releaseDate: Date?,
    val title: String,
    val voteAverage: Float,
    val voteCount: Int,

    val backdropUrl: String? = null,
    val posterUrl: String? = null,

    val scheduled: Date? = null
)
