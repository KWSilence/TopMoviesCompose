package com.kwsilence.topmoviescompose.data.exception

import com.kwsilence.topmoviescompose.domain.exception.RemoteSourceException

class TimeoutException(cause: Throwable) : RemoteSourceException(cause = cause)
