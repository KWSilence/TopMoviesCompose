package com.kwsilence.topmoviescompose.domain.model

import java.util.Date

data class MovieDetails (
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

    val homepage: String?,
    val budget: Long?,
    val runtime: Int?,
    val revenue: Long?,
    val genres: List<Genre>?,
    val status: String?,
    val tagline: String?,

    val backdropUrl: String? = null,
    val posterUrl: String? = null,

    val scheduled: Date? = null
)
