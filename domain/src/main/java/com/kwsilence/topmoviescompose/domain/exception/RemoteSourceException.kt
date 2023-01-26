package com.kwsilence.topmoviescompose.domain.exception

open class RemoteSourceException(
    cause: Throwable? = null,
    message: String? = cause?.localizedMessage
) : Exception(message, cause)
