package com.kwsilence.topmoviescompose.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kwsilence.topmoviescompose.navigation.bottom.TopMoviesBottomNavigationBar
import com.kwsilence.topmoviescompose.navigation.bottom.TopMoviesBottomNavigationItem
import com.kwsilence.topmoviescompose.navigation.top.TopMoviesTopAppBar
import com.kwsilence.topmoviescompose.navigation.top.TopMoviesTopAppBarItem

@Composable
fun TopMoviesScaffold(
    navController: NavHostController,
    title: String? = null,
    topBarActions: List<TopMoviesTopAppBarItem>? = null,
    showTopBar: Boolean = true,
    showBack: Boolean? = null,
    bottomNavigationItemList: List<TopMoviesBottomNavigationItem>? = null,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            TopMoviesBottomNavigationBar(
                navController = navController,
                items = bottomNavigationItemList ?: emptyList(),
                visible = !bottomNavigationItemList.isNullOrEmpty()
            )
        },
        topBar = {
            TopMoviesTopAppBar(
                navController = navController,
                title = title ?: "",
                showBack = showBack,
                actions = topBarActions,
                visible = showTopBar
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}
