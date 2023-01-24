package com.kwsilence.topmoviescompose.navigation

import androidx.navigation.NavHostController

class NavGraph(val navController: NavHostController) {
    fun openMovieList() {
        navController.navigate(Screen.MovieList.route)
    }

    fun openScheduleList() {
        navController.navigate(Screen.ScheduleList.route)
    }

    fun openMovieDetails(id: Int) {
        navController.navigate("${Screen.MovieDetails.route}/$id")
    }

    fun openScheduleTime(id: Int) {
        navController.navigate("${Screen.ScheduleTime.route}/$id")
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}
