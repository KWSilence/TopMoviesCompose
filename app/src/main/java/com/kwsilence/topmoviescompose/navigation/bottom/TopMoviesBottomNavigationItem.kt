package com.kwsilence.topmoviescompose.navigation.bottom

import com.kwsilence.topmoviescompose.R

sealed class TopMoviesBottomNavigationItem(val title: String, val icon: Int, val route: String) {
    object MovieList : TopMoviesBottomNavigationItem(
        title = "Popular movies",
        icon = R.drawable.ic_movies,
        route = "movie_list"
    )

    object ScheduleList : TopMoviesBottomNavigationItem(
        title = "Scheduled movies",
        icon = R.drawable.ic_history,
        route = "schedule_list"
    )

    companion object {
        val mainBottomNavigation: List<TopMoviesBottomNavigationItem> = listOf(MovieList, ScheduleList)
    }
}
