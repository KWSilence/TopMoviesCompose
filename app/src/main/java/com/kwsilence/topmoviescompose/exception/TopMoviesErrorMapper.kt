package com.kwsilence.topmoviescompose.exception

import com.kwsilence.topmoviescompose.R
import com.kwsilence.topmoviescompose.data.exception.HostConnectionException
import com.kwsilence.topmoviescompose.data.exception.LocalMovieNotFoundException
import com.kwsilence.topmoviescompose.data.exception.TimeoutException
import com.kwsilence.topmoviescompose.data.exception.UnknownException

fun Throwable.toTopMoviesError(): TopMoviesError = when (this) {
    is LocalMovieNotFoundException -> R.string.error_local_movie_not_found
    is TimeoutException -> R.string.error_timeout
    is HostConnectionException -> R.string.error_host_connection
//    is InternetConnectionException -> R.string.error_internet_connection
    is UnknownException -> R.string.error_unknown_error
    else -> null
}.let { stringResId -> TopMoviesError(stringResId = stringResId, exception = this) }
