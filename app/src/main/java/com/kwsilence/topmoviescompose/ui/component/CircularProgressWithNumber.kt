package com.kwsilence.topmoviescompose.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CircularProgressWithNumber(modifier: Modifier = Modifier, progress: Float) {
    Box(modifier = modifier) {
        val progressModifier = Modifier.size(30.dp)
        val strokeWidth = 2.dp
        val fontSize = 12.sp
        val progressColor = MaterialTheme.colors.primary
        CircularProgressIndicator(
            modifier = progressModifier,
            progress = 1f,
            strokeWidth = strokeWidth,
            color = progressColor.copy(alpha = 0.25f)
        )
        CircularProgressIndicator(
            modifier = progressModifier,
            progress = progress,
            strokeWidth = strokeWidth,
            color = progressColor
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = (progress * 100).roundToInt().toString(),
            fontSize = fontSize
        )
    }
}

@Composable
@Preview
private fun CircularProgressWithNumberPreview() {
    CircularProgressWithNumber(progress = 0.5f)
}
