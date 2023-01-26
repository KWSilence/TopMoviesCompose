package com.kwsilence.topmoviescompose.feature.schedule.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwsilence.topmoviescompose.R
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.exception.getErrorString
import com.kwsilence.topmoviescompose.navigation.NavGraph
import com.kwsilence.topmoviescompose.navigation.Screen
import com.kwsilence.topmoviescompose.navigation.bottom.TopMoviesBottomNavigationItem
import com.kwsilence.topmoviescompose.navigation.scaffold.TopMoviesScaffold
import com.kwsilence.topmoviescompose.ui.component.OutlinedButtonWithText
import com.kwsilence.topmoviescompose.ui.component.SubmitDialog
import com.kwsilence.topmoviescompose.ui.util.showToast
import com.kwsilence.topmoviescompose.util.FormatUtils
import org.koin.androidx.compose.koinViewModel
import java.util.Date

private val titleTextStyle = TextStyle.Default.copy(
    textAlign = TextAlign.Center,
    fontWeight = FontWeight.Bold
)

@Composable
private fun Date?.toScheduleString(): String =
    this?.let { date ->
        FormatUtils.formatScheduleDate(date)
    } ?: stringResource(id = R.string.date_not_presented)

@Composable
fun ScheduleListScreen(navGraph: NavGraph) {
    val viewModel: ScheduleListViewModel = koinViewModel()
    val state = viewModel.state
    val movieListState = viewModel.scheduledMovieListFlow.collectAsState(initial = state.movieList)
    val movieList = movieListState.value
    viewModel.saveMovieListChanges(movieList)
    var movieToDelete: Movie? by remember { mutableStateOf(null) }

    state.error?.content?.let { error ->
        LocalContext.current.showToast(error.getErrorString())
    }
    state.deletedMovieSchedule?.content?.let {
        LocalContext.current.showToast(stringResource(id = R.string.message_movie_schedule_deleted))
    }

    movieToDelete?.let { movie ->
        SubmitDialog(
            title = stringResource(id = R.string.dialog_delete_schedule_title),
            message = stringResource(id = R.string.dialog_delete_schedule_message).format(
                movie.title,
                movie.scheduled.toScheduleString()
            ),
            onDismiss = { movieToDelete = null },
            onPositive = { viewModel.deleteSchedule(movie) }
        )
    }

    TopMoviesScaffold(
        navController = navGraph.navController,
        title = stringResource(id = Screen.ScheduleList.titleResId),
        showBack = false,
        bottomNavigationItemList = TopMoviesBottomNavigationItem.mainBottomNavigation
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            when (movieList.isEmpty()) {
                true -> {
                    item {
                        NoScheduledMoviesCard()
                    }
                }
                false -> {
                    items(movieList) { movie ->
                        ScheduledMovieCard(
                            movie = movie,
                            onMovieClick = { navGraph.openMovieDetails(movie.id) },
                            onScheduleClick = { navGraph.openScheduleTime(movie.id) },
                            onDeleteClick = { movieToDelete = movie }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleListItemCard(content: @Composable () -> Unit) {
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
fun ScheduledMovieCard(
    movie: Movie,
    onMovieClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ScheduleListItemCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMovieClick() }
                .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = movie.title,
                style = titleTextStyle
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButtonWithText(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = movie.scheduled.toScheduleString(),
                    onClick = { onScheduleClick() }
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    modifier = Modifier
                        .clickable { onDeleteClick() }
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = stringResource(id = R.string.description_delete_icon),
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun NoScheduledMoviesCard() {
    ScheduleListItemCard {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = stringResource(id = R.string.no_scheduled_movies),
            textAlign = TextAlign.Center
        )
    }
}
