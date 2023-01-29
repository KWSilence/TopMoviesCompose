package com.kwsilence.topmoviescompose.feature.movie.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kwsilence.topmoviescompose.R
import com.kwsilence.topmoviescompose.domain.model.MovieDetails
import com.kwsilence.topmoviescompose.exception.getErrorString
import com.kwsilence.topmoviescompose.navigation.NavGraph
import com.kwsilence.topmoviescompose.navigation.Screen
import com.kwsilence.topmoviescompose.navigation.scaffold.TopMoviesScaffold
import com.kwsilence.topmoviescompose.ui.component.AsyncImageWithProgress
import com.kwsilence.topmoviescompose.ui.component.CircularProgressWithNumber
import com.kwsilence.topmoviescompose.ui.component.OutlinedButtonWithText
import com.kwsilence.topmoviescompose.ui.util.showToast
import com.kwsilence.topmoviescompose.util.FormatUtils
import org.koin.androidx.compose.koinViewModel

private val imageWidth: Dp = 150.dp
private const val imageRatio: Float = 2f / 3f

private val titleTextStyle = TextStyle.Default.copy(
    fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold
)
private val dateTextStyle = TextStyle.Default.copy(
    fontSize = 14.sp,
    lineHeight = 18.sp,
    fontWeight = FontWeight.Light,
    fontStyle = FontStyle.Italic
)
private val propertyTextStyle = TextStyle.Default.copy(fontSize = 14.sp, lineHeight = 18.sp)
private val overviewPropertyTextStyle = TextStyle.Default.copy(
    fontSize = 14.sp, fontWeight = FontWeight.Bold
)
private val overviewTextStyle = TextStyle.Default.copy(
    fontSize = 14.sp, lineHeight = 18.sp, textAlign = TextAlign.Justify
)

private fun Long.moneyToString() = FormatUtils.formatMoney(this)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieDetailsScreen(navGraph: NavGraph, id: Int?) {
    val viewModel: MovieDetailsViewModel = koinViewModel()
    val state = viewModel.state

    state.error?.content?.let { error ->
        LocalContext.current.showToast(error.getErrorString())
    }
    val refreshState = rememberPullRefreshState(refreshing = state.isRefreshing, onRefresh = {
        id?.let { movieId ->
            viewModel.getMovieDetails(
                id = movieId, force = true
            )
        }
    })

    TopMoviesScaffold(
        navController = navGraph.navController,
        title = stringResource(id = Screen.MovieDetails.titleResId),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = refreshState, enabled = id != null),
            contentAlignment = Alignment.Center
        ) {
            when (id) {
                null -> Text(text = stringResource(id = R.string.movie_id_not_found))
                else -> {
                    val detailsState = viewModel.getFlowMovieDetails(id)
                        .collectAsState(initial = state.details)
                    when (val details = detailsState.value) {
                        null -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                contentAlignment = Alignment.Center
                            ) {
                                when (state.error) {
                                    null -> viewModel.getMovieDetails(id)
                                    else -> Text(text = stringResource(id = R.string.pull_to_refresh))
                                }
                            }
                        }
                        else -> MovieDetailsInfo(navGraph, details = details)
                    }
                    PullRefreshIndicator(
                        refreshing = state.isRefreshing,
                        state = refreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsInfo(navGraph: NavGraph, details: MovieDetails) {
    val showFullScreenPoster = remember { mutableStateOf(false) }

    if (showFullScreenPoster.value) {
        ZoomablePosterDialog(
            posterUrl = details.posterUrl,
            onDismiss = { showFullScreenPoster.value = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImageWithProgress(
                    modifier = Modifier
                        .clickable { showFullScreenPoster.value = true }
                        .requiredWidth(imageWidth)
                        .aspectRatio(imageRatio)
                        .clip(MaterialTheme.shapes.medium),
                    url = details.posterUrl
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Text(text = details.title, style = titleTextStyle)
                    Text(
                        text = details.releaseDate?.let { releaseDate ->
                            FormatUtils.formatMovieReleaseDate(releaseDate)
                        } ?: stringResource(id = R.string.date_not_presented),
                        style = dateTextStyle
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    Row {
                        CircularProgressWithNumber(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            progress = details.voteAverage / 10f
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Column {
                            PropertyText(
                                property = stringResource(id = R.string.property_vote),
                                value = details.voteCount
                            )
                            PropertyText(
                                property = stringResource(id = R.string.property_popularity),
                                value = details.popularity
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    PropertyText(
                        property = stringResource(id = R.string.property_runtime),
                        value = details.runtime?.let { "$it min" }
                    )
                    PropertyText(
                        property = stringResource(id = R.string.property_status),
                        value = details.status
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    PropertyText(
                        property = stringResource(id = R.string.property_budget),
                        value = details.budget?.moneyToString()
                    )
                    PropertyText(
                        property = stringResource(id = R.string.property_revenue),
                        value = details.revenue?.moneyToString()
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            PropertyText(
                property = stringResource(id = R.string.property_original_language),
                value = details.originalLang
            )
            PropertyText(
                property = stringResource(id = R.string.property_original_title),
                value = details.originalTitle
            )
            Spacer(modifier = Modifier.height(5.dp))
            PropertyText(
                property = stringResource(id = R.string.property_genres),
                value = details.genres?.joinToString { it.name }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${stringResource(id = R.string.property_overview)}:",
                style = overviewPropertyTextStyle
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .weight(1f),
                text = details.overview ?: stringResource(id = R.string.property_not_presented),
                style = overviewTextStyle
            )
        }
        OutlinedButtonWithText(
            modifier = Modifier.fillMaxWidth(),
            text = when (val schedule = details.scheduled) {
                null -> stringResource(id = R.string.schedule_watching)
                else -> FormatUtils.formatScheduleDate(schedule)
            },
            onClick = { navGraph.openScheduleTime(details.id) }
        )
    }
}

@Composable
private fun ZoomablePosterDialog(posterUrl: String?, onDismiss: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val interactionSource = remember { MutableInteractionSource() }
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onDismiss
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f..4f)

                        val maxX = imageSize.width * (scale - 1) / 2
                        val maxY = imageSize.height * (scale - 1) / 2

                        offsetX = (offsetX + pan.x).coerceIn(-maxX..maxX)
                        offsetY = (offsetY + pan.y).coerceIn(-maxY..maxY)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImageWithProgress(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { imageSize = it }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                url = posterUrl
            )
        }
    }
}

@Composable
private fun PropertyText(property: String, value: Any?) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("$property: ") }
            append(value?.toString() ?: stringResource(id = R.string.property_not_presented))
        },
        style = propertyTextStyle
    )
}
