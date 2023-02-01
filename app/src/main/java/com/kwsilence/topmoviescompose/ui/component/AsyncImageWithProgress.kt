package com.kwsilence.topmoviescompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.imageLoader
import coil.request.ImageRequest
import org.koin.androidx.compose.get

@Composable
fun AsyncImageWithProgress(
    modifier: Modifier = Modifier,
    url: String?,
    contentDescription: String? = null,
    imageLoader: ImageLoader = LocalContext.current.imageLoader
) {
    when (url) {
        null -> Box(
            modifier = modifier.background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No Image")
        }
        else -> SubcomposeAsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
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
