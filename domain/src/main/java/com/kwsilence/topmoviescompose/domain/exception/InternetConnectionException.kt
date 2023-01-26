package com.kwsilence.topmoviescompose.domain.exception

import com.kwsilence.topmoviescompose.domain.util.InternetConnectionChecker

class InternetConnectionException(cause: Throwable) : Exception(cause)

internal fun <T> Result<T>.detectInternetConnectionException(
    connectionChecker: InternetConnectionChecker
): Result<T> =
    this.fold(
        onSuccess = { Result.success(it) },
        onFailure = { exception ->
            when {
                exception is RemoteSourceException && !connectionChecker.checkConnection() -> {
                    InternetConnectionException(exception)
                }
                else -> exception
            }.let { Result.failure(it) }
        }
    )
