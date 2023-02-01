package com.kwsilence.topmoviescompose.feature.schedule.time

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwsilence.topmoviescompose.R
import com.kwsilence.topmoviescompose.domain.model.stubMovieDetails
import com.kwsilence.topmoviescompose.exception.getErrorString
import com.kwsilence.topmoviescompose.navigation.NavGraph
import com.kwsilence.topmoviescompose.navigation.Screen
import com.kwsilence.topmoviescompose.navigation.scaffold.TopMoviesScaffold
import com.kwsilence.topmoviescompose.navigation.top.TopMoviesTopAppBarItem
import com.kwsilence.topmoviescompose.ui.component.OutlinedButtonWithText
import com.kwsilence.topmoviescompose.ui.component.SubmitDialog
import com.kwsilence.topmoviescompose.ui.util.showToast
import com.kwsilence.topmoviescompose.util.FormatUtils
import org.koin.androidx.compose.koinViewModel
import java.util.Date

private val titleTextStyle = TextStyle.Default.copy(
    fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold
)
private val previousScheduleTextStyle = TextStyle.Default.copy(
    fontSize = 12.sp, lineHeight = 16.sp, fontStyle = FontStyle.Italic
)

private fun Date?.toScheduleString(): String? = this?.let { date ->
    FormatUtils.formatScheduleDate(date)
}

private fun Date.toDayString(): String = FormatUtils.formatScheduleDay(this)
private fun Date.toTimeString(): String = FormatUtils.formatScheduleTime(this)

private enum class ScheduleTimeDialogState {
    NONE, SUBMIT, DELETE
}

@Composable
fun ScheduleTimeScreen(
    navGraph: NavGraph,
    id: Int?
) {
    val viewModel: ScheduleTimeViewModel = koinViewModel()
    val state = viewModel.state
    var dialogState by remember { mutableStateOf(ScheduleTimeDialogState.NONE) }

    state.error?.content?.let { error ->
        LocalContext.current.showToast(error.getErrorString())
    }

    when {
        state.scheduleCompleted.content == true -> stringResource(id = R.string.message_movie_scheduled)
        state.deleteCompleted.content == true -> stringResource(id = R.string.message_movie_schedule_deleted)
        else -> null
    }?.let { message ->
        LocalContext.current.showToast(message)
        navGraph.navigateUp()
    }

    when (dialogState) {
        ScheduleTimeDialogState.DELETE -> DeleteScheduleDialog(
            movieTitle = state.movie?.title,
            scheduleDate = state.movie?.scheduled,
            onDismiss = { dialogState = ScheduleTimeDialogState.NONE },
            onSubmit = { viewModel.deleteSchedule() }
        )
        ScheduleTimeDialogState.SUBMIT -> ScheduleWatchingDialog(
            movieTitle = state.movie?.title,
            scheduleDate = state.dateTime,
            onDismiss = { dialogState = ScheduleTimeDialogState.NONE },
            onSubmit = { viewModel.submitTime() }
        )
        else -> Unit
    }

    TopMoviesScaffold(
        navController = navGraph.navController,
        title = stringResource(id = Screen.ScheduleTime.titleResId),
        topBarActions = when (state.movie?.scheduled) {
            null -> null
            else -> listOf(
                TopMoviesTopAppBarItem(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    description = stringResource(id = R.string.description_delete_icon),
                    onClick = { dialogState = ScheduleTimeDialogState.DELETE }
                )
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (id) {
                null -> Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.movie_id_not_found)
                )
                else -> {
                    when (state.movie == null && state.error == null) {
                        true -> {
                            viewModel.loadMovieInfo(id)
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        false -> ScheduleTimeView(
                            state = state,
                            onSetDate = { year, month, day -> viewModel.setDate(year, month, day) },
                            onSetTime = { hour, minute -> viewModel.setTime(hour, minute) },
                            onSubmit = { dialogState = ScheduleTimeDialogState.SUBMIT }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteScheduleDialog(
    movieTitle: String?,
    scheduleDate: Date?,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    SubmitDialog(
        title = stringResource(id = R.string.dialog_delete_schedule_title),
        message = stringResource(id = R.string.dialog_delete_schedule_message).format(
            movieTitle,
            scheduleDate?.let { date -> FormatUtils.formatScheduleDate(date) }
        ),
        onDismiss = onDismiss,
        onPositive = onSubmit
    )
}

@Composable
private fun ScheduleWatchingDialog(
    movieTitle: String?,
    scheduleDate: Date?,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    SubmitDialog(
        title = stringResource(id = R.string.dialog_schedule_title),
        message = stringResource(id = R.string.dialog_schedule_message).format(
            movieTitle,
            scheduleDate?.let { date -> FormatUtils.formatScheduleDate(date) }
        ),
        onDismiss = onDismiss,
        onPositive = onSubmit
    )
}

@Composable
private fun DatePickerButton(
    state: ScheduleTimeScreenState,
    onSetDate: (year: Int, month: Int, day: Int) -> Unit
) {
    val datePicker = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day -> onSetDate(year, month, day) },
        state.year,
        state.month,
        state.day
    )
    OutlinedButtonWithText(
        modifier = Modifier.fillMaxWidth(),
        text = state.dateTime?.toDayString() ?: stringResource(id = R.string.day_not_set),
        onClick = { datePicker.show() }
    )
}

@Composable
private fun TimePickerButton(
    state: ScheduleTimeScreenState,
    onSetTime: (hour: Int, minute: Int) -> Unit
) {
    val timePicker = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute -> onSetTime(hour, minute) },
        state.hour,
        state.minute,
        true
    )
    OutlinedButtonWithText(
        modifier = Modifier.fillMaxWidth(),
        text = state.dateTime?.toTimeString() ?: stringResource(id = R.string.time_not_set),
        onClick = { timePicker.show() }
    )
}

@Composable
private fun BoxScope.ScheduleTimeView(
    state: ScheduleTimeScreenState,
    onSetDate: (year: Int, month: Int, day: Int) -> Unit,
    onSetTime: (hour: Int, minute: Int) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(0.6f)
            .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = state.movie?.title ?: "",
            style = titleTextStyle,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        DatePickerButton(state = state, onSetDate = onSetDate)
        Spacer(modifier = Modifier.height(10.dp))
        TimePickerButton(state = state, onSetTime = onSetTime)
        Spacer(modifier = Modifier.height(20.dp))
        state.movie?.scheduled?.let { schedule ->
            Text(
                text = stringResource(id = R.string.previous_schedule).format(
                    FormatUtils.formatScheduleDate(schedule)
                ),
                style = previousScheduleTextStyle
            )
        }
        OutlinedButtonWithText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.submit_schedule),
            onClick = { onSubmit() }
        )
    }
}

@Composable
@Preview
private fun ScheduleTimeViewPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        ScheduleTimeView(
            state = ScheduleTimeScreenState(
                dateTime = Date(),
                movie = stubMovieDetails.copy(title = "Movie title")
            ),
            onSetDate = { _, _, _ -> },
            onSetTime = { _, _ -> },
            onSubmit = {}
        )
    }
}
