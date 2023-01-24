package com.kwsilence.topmoviescompose.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CircularProgressWithNumber(modifier: Modifier = Modifier, progress: Float) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            progress = 1f,
            strokeWidth = 2.dp,
            color = MaterialTheme.colors.primary.copy(alpha = 0.25f)
        )
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            progress = progress,
            strokeWidth = 2.dp,
            color = MaterialTheme.colors.primary
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = (progress * 100).roundToInt().toString(),
            fontSize = 12.sp
        )
    }
}
