package com.kwsilence.topmoviescompose.domain.model

fun MovieDetails.toMovie(): Movie =
    Movie(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        originalLang = originalLang,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl
    )

val stubMovieDetails
    get() = MovieDetails(
        adult = false,
        backdropPath = null,
        id = 0,
        originalLang = "",
        originalTitle = "",
        overview = null,
        popularity = 0f,
        posterPath = null,
        releaseDate = null,
        title = "",
        voteAverage = 0f,
        voteCount = 0,
        homepage = null,
        budget = null,
        runtime = null,
        revenue = null,
        genres = null,
        status = null,
        tagline = null,
        backdropUrl = null,
        posterUrl = null,
        scheduled = null
    )
