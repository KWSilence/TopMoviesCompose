package com.kwsilence.topmoviescompose.navigation.top

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kwsilence.topmoviescompose.R

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

private fun navigateUpIcon(navController: NavHostController): @Composable () -> Unit = {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.description_back)
        )
    }
}

@Composable
@Preview
private fun TopMoviesTopAppBarPreview() {
    TopMoviesTopAppBar(
        navController = rememberNavController(),
        title = "App bar title",
        showBack = true,
        actions = listOf(
            TopMoviesTopAppBarItem(
                imageVector = Icons.Filled.Search,
                description = null,
                onClick = {}
            )
        ),
        visible = true
    )
}
