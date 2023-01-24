package com.kwsilence.topmoviescompose.navigation.top

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun TopMoviesTopAppBar(
    navController: NavHostController,
    title: String,
    showBack: Boolean? = null,
    actions: List<TopMoviesTopAppBarItem>? = null,
    visible: Boolean = true
) {
    if (!visible) return
    val hasPrevious = navController.previousBackStackEntry != null
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = when (showBack == true || (showBack == null && hasPrevious)) {
            true -> navigateUpIcon(navController)
            false -> null
        },
        actions = {
            actions?.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(imageVector = action.imageVector, contentDescription = action.description)
                }
            }
        }
    )
}

private fun navigateUpIcon(navController: NavHostController): @Composable () -> Unit =
    {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }
    }
