package com.kwsilence.topmoviescompose.exception

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

data class TopMoviesError(
    val stringResId: Int? = null,
    val exception: Throwable
)

@Composable
fun TopMoviesError.getErrorString(): String? = when (stringResId) {
    null -> exception.localizedMessage
    else -> stringResource(id = stringResId)
}
