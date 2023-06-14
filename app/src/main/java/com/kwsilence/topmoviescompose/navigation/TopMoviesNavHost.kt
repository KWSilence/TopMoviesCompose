package com.kwsilence.topmoviescompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kwsilence.topmoviescompose.feature.movie.details.MovieDetailsScreen
import com.kwsilence.topmoviescompose.feature.movie.list.MovieListScreen
import com.kwsilence.topmoviescompose.feature.schedule.list.ScheduleListScreen
import com.kwsilence.topmoviescompose.feature.schedule.time.ScheduleTimeScreen

@Composable
fun TopMoviesNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navGraph = NavGraph(navController)
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.MovieList.route
    ) {
        composable(route = Screen.MovieList.route) {
            MovieListScreen(navGraph = navGraph)
        }
        composable(
            route = Screen.MovieDetails.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            ),
            deepLinks = listOf(ScreenDeepLink.MovieDetails.toNavDeepLink())
        ) { entry ->
            MovieDetailsScreen(navGraph = navGraph, id = entry.arguments?.getInt("id"))
        }
        composable(
            route = Screen.ScheduleTime.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            ScheduleTimeScreen(navGraph = navGraph, id = entry.arguments?.getInt("id"))
        }
        composable(route = Screen.ScheduleList.route) {
            ScheduleListScreen(navGraph = navGraph)
        }
    }
}
