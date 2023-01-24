package com.kwsilence.topmoviescompose.navigation

sealed class Screen(val title: String, val route: String) {
    object MovieList : Screen(title = "Popular movies", route = "movie_list")
    object ScheduleList : Screen(title = "Scheduled movies", route = "schedule_list")
    object MovieDetails : Screen(title = "Movie details", route = "movie_details")
    object ScheduleTime : Screen(title = "Schedule watching", route = "schedule_time")
}
