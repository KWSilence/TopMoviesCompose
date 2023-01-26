package com.kwsilence.topmoviescompose.data.exception

import com.kwsilence.topmoviescompose.domain.exception.RemoteSourceException

class HostConnectionException(cause: Throwable) : RemoteSourceException(cause = cause)
