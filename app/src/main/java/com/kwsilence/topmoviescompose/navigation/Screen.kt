package com.kwsilence.topmoviescompose.navigation

import androidx.annotation.StringRes
import com.kwsilence.topmoviescompose.R

sealed class Screen(@StringRes val titleResId: Int, val route: String) {
    object MovieList : Screen(
        titleResId = R.string.title_popular_movies,
        route = "movie_list"
    )

    object ScheduleList : Screen(
        titleResId = R.string.title_scheduled_movies,
        route = "schedule_list"
    )

    object MovieDetails : Screen(
        titleResId = R.string.title_movie_details,
        route = "movie_details"
    )

    object ScheduleTime :
        Screen(
            titleResId = R.string.title_schedule_watching,
            route = "schedule_time"
        )
}
