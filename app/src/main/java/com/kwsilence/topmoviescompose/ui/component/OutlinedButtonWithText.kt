package com.kwsilence.topmoviescompose.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedButtonWithText(
    modifier: Modifier,
    text: String,
    textStyle: TextStyle = TextStyle.Default,
    borderWidth: Dp = 1.dp,
    color: Color = MaterialTheme.colors.primary,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .border(width = borderWidth, color = color, shape = shape)
            .clip(shape)
            .clickable { onClick() }
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
            color = color,
            style = textStyle
        )
    }
}

@Composable
@Preview
private fun OutlinedButtonWithTextPreview() {
    OutlinedButtonWithText(
        modifier = Modifier
            .padding(5.dp),
        text = "Button text",
        onClick = {}
    )
}
