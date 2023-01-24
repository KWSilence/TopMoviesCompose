package com.kwsilence.topmoviescompose.feature.movie.details

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwsilence.topmoviescompose.domain.model.MovieDetails
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

    state.error?.content?.let { errorMessage ->
        LocalContext.current.showToast(errorMessage)
    }
    val refreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { id?.let { movieId -> viewModel.getMovieDetails(id = movieId, force = true) } }
    )

    TopMoviesScaffold(
        navController = navGraph.navController,
        title = Screen.MovieDetails.title,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = refreshState, enabled = id != null),
            contentAlignment = Alignment.Center
        ) {
            when (id) {
                null -> Text(text = "Film ID not found :(")
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
                                    else -> Text(text = "Pull to refresh")
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
                        } ?: "None",
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
                            PropertyText(property = "Vote", value = details.voteCount)
                            PropertyText(property = "Popularity", value = details.popularity)
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    PropertyText(property = "Runtime", value = details.runtime?.let { "$it min" })
                    PropertyText(property = "Status", value = details.status)
                    Spacer(modifier = Modifier.height(3.dp))
                    PropertyText(property = "Budget", value = details.budget?.moneyToString())
                    PropertyText(property = "Revenue", value = details.revenue?.moneyToString())
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            PropertyText(property = "Original Language", value = details.originalLang)
            PropertyText(property = "Original Title", value = details.originalTitle)
            Spacer(modifier = Modifier.height(5.dp))
            PropertyText(property = "Genres", value = details.genres?.joinToString { it.name })
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Overview:", style = overviewPropertyTextStyle)
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .weight(1f),
                text = details.overview ?: "Overview is not presented.",
                style = overviewTextStyle
            )
        }
        OutlinedButtonWithText(
            modifier = Modifier.fillMaxWidth(),
            text = when (val schedule = details.scheduled) {
                null -> "Schedule watching"
                else -> FormatUtils.formatScheduleDate(schedule)
            },
            onClick = { navGraph.openScheduleTime(details.id) }
        )
    }
}

@Composable
private fun PropertyText(property: String, value: Any?) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("$property: ") }
            append(value?.toString() ?: "Not presented")
        },
        style = propertyTextStyle
    )
}
