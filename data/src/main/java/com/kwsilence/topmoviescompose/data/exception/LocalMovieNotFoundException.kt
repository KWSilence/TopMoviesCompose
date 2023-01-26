package com.kwsilence.topmoviescompose.data.exception

class LocalMovieNotFoundException(
    movieId: Int? = null
) : Exception("Local movie not found" + (movieId?.let { "(id=$movieId)" } ?: ""))
