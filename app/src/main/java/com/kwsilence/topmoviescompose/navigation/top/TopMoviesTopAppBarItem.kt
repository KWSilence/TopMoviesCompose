package com.kwsilence.topmoviescompose.navigation.top

import androidx.compose.ui.graphics.vector.ImageVector

data class TopMoviesTopAppBarItem(
    val imageVector: ImageVector,
    val description: String? = null,
    val onClick: () -> Unit
)
