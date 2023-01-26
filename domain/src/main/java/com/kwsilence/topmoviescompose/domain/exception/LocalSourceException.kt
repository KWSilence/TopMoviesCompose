package com.kwsilence.topmoviescompose.domain.exception

open class LocalSourceException(
    cause: Throwable? = null,
    message: String? = cause?.localizedMessage
) : Exception(message, cause)
