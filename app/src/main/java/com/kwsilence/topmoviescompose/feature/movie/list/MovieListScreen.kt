package com.kwsilence.topmoviescompose.feature.movie.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwsilence.topmoviescompose.R
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.exception.getErrorString
import com.kwsilence.topmoviescompose.navigation.NavGraph
import com.kwsilence.topmoviescompose.navigation.Screen
import com.kwsilence.topmoviescompose.navigation.bottom.TopMoviesBottomNavigationItem
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
    fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold
)
private val dateTextStyle = TextStyle.Default.copy(
    fontSize = 12.sp, fontWeight = FontWeight.Light, fontStyle = FontStyle.Italic
)
private val overviewTextStyle = TextStyle.Default.copy(
    fontSize = 12.sp, lineHeight = 16.sp, textAlign = TextAlign.Justify
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieListScreen(navGraph: NavGraph) {
    val viewModel: MovieListViewModel = koinViewModel()
    val state = viewModel.state
    val movieListState = viewModel.getFlowMovieList(state.currentPage)
        .collectAsState(initial = state.movieList)
    viewModel.saveMovieListChanges(movieList = movieListState.value)
    val movieList = state.movieList

    state.error?.content?.let { error ->
        LocalContext.current.showToast(error.getErrorString())
    }
    val refreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    TopMoviesScaffold(
        navController = navGraph.navController,
        title = stringResource(id = Screen.MovieList.titleResId),
        showBack = false,
        bottomNavigationItemList = TopMoviesBottomNavigationItem.mainBottomNavigation
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = refreshState, enabled = true)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(movieList) { index, movie ->
                    if (index >= movieList.size - 1) viewModel.loadMoreMovies()
                    MovieCard(navGraph = navGraph, movie = movie)
                }
                item {
                    if (state.showRetry)
                        RetryCard(onRetry = { viewModel.loadMoreMovies(retry = true) })
                }
                item {
                    if (state.isLoading && state.canLoadMore) LoadingCard()
                }
            }
            if (state.error != null && movieList.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.pull_to_refresh)
                )
            }
            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun MovieListItemCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ),
        content = content
    )
}

@Composable
private fun MovieCard(navGraph: NavGraph, movie: Movie) {
    MovieListItemCard {
        val collapsedOverviewState = rememberSaveable { mutableStateOf<Boolean?>(null) }
        val collapsed = collapsedOverviewState.value
        Row(
            modifier = Modifier
                .clickable { navGraph.openMovieDetails(movie.id) }
                .padding(5.dp)
                .fillMaxWidth()
                .run {
                    when (collapsed) {
                        false -> wrapContentHeight()
                        else -> height(imageWidth / imageRatio)
                    }
                }
        ) {
            AsyncImageWithProgress(
                modifier = Modifier
                    .requiredWidth(imageWidth)
                    .aspectRatio(imageRatio)
                    .clip(MaterialTheme.shapes.medium),
                url = movie.posterUrl
            )
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Row {
                    CircularProgressWithNumber(progress = movie.voteAverage / 10f)
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(text = movie.title, style = titleTextStyle)
                        Text(
                            text = movie.releaseDate?.let { releaseDate ->
                                FormatUtils.formatMovieReleaseDate(releaseDate)
                            } ?: stringResource(id = R.string.date_not_presented),
                            style = dateTextStyle
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                CollapsingText(
                    modifier = Modifier.run {
                        when (collapsed) {
                            false -> wrapContentHeight()
                            else -> weight(1f)
                        }
                    },
                    text = movie.overview ?: stringResource(id = R.string.overview_not_presented),
                    style = overviewTextStyle,
                    collapseState = collapsedOverviewState
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedButtonWithText(
                    modifier = Modifier.fillMaxWidth(),
                    text = when (val schedule = movie.scheduled) {
                        null -> stringResource(id = R.string.schedule_watching)
                        else -> FormatUtils.formatScheduleDate(schedule)
                    },
                    onClick = { navGraph.openScheduleTime(movie.id) }
                )
            }
        }
    }
}

@Composable
private fun CollapsingText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle.Default,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    collapseState: MutableState<Boolean?> = remember { mutableStateOf(null) }
) {
    val collapsed = collapseState.value
    Column(
        modifier = modifier.run {
            collapsed?.let { clickable { collapseState.value = !it } } ?: this
        }
    ) {
        Text(
            modifier = Modifier.run {
                when (collapsed) {
                    false -> wrapContentHeight()
                    else -> weight(1f)
                }
            },
            text = text,
            style = style,
            overflow = overflow,
            onTextLayout = { textLayoutResult ->
                collapseState.value = when (textLayoutResult.hasVisualOverflow) {
                    true -> collapsed ?: true
                    false -> collapsed
                }
            }
        )
        if (collapsed != null) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                val collapseRotate = if (collapsed) 0f else 180f
                val collapseDescription = when (collapsed) {
                    true -> R.string.description_expand
                    false -> R.string.description_collapse
                }.let { descriptionRes -> stringResource(id = descriptionRes) }
                Icon(
                    modifier = Modifier.rotate(collapseRotate),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = collapseDescription
                )
            }
        }
    }
}

@Composable
private fun RetryCard(
    messageText: String = stringResource(id = R.string.retry_loading),
    retryText: String = stringResource(id = R.string.retry),
    onRetry: () -> Unit
) {
    MovieListItemCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = messageText,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .clickable { onRetry() }
                    .padding(10.dp),
                text = retryText,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun LoadingCard() {
    MovieListItemCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
