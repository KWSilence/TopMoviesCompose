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
