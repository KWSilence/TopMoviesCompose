package com.kwsilence.topmoviescompose.feature.schedule.list

import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.util.Event

data class ScheduleListScreenState(
    val movieList: List<Movie> = emptyList(),
    val deletedMovieSchedule: Event<Movie>? = null,
    val error: Event<String>? = null
)
