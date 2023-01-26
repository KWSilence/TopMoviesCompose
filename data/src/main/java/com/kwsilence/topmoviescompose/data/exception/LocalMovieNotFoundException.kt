package com.kwsilence.topmoviescompose.data.exception

import com.kwsilence.topmoviescompose.domain.exception.LocalSourceException

class LocalMovieNotFoundException(
    movieId: Int? = null
) : LocalSourceException(message = "Local movie not found (id=$movieId)")
