package com.kwsilence.topmoviescompose.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest

@Composable
fun AsyncImageWithProgress(
    modifier: Modifier = Modifier,
    url: String?,
    contentDescription: String? = null
) {
    when (url) {
        null -> Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(text = "No Image")
        }
        else -> SubcomposeAsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .diskCacheKey(url)
                .memoryCacheKey(url)
                .build(),
            contentDescription = contentDescription
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> Box {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                else -> SubcomposeAsyncImageContent()
            }
        }
    }
}
