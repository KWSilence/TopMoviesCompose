package com.kwsilence.topmoviescompose.navigation.scaffold

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
    showBottomBar: Boolean = !bottomNavigationItemList.isNullOrEmpty(),
    content: @Composable () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()
    val showUiColor = MaterialTheme.colors.primarySurface
    val hideUiColor = MaterialTheme.colors.background
    val systemIiColor: (Boolean) -> Color = { show ->
        if (show) showUiColor else hideUiColor
    }
    SideEffect {
        systemUiController.apply {
            setStatusBarColor(
                color = systemIiColor(showTopBar),
                darkIcons = darkTheme
            )
            setNavigationBarColor(
                color = systemIiColor(showBottomBar),
                darkIcons = darkTheme
            )
        }
    }

    Scaffold(
        bottomBar = {
            TopMoviesBottomNavigationBar(
                navController = navController,
                items = bottomNavigationItemList ?: emptyList(),
                visible = showBottomBar
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
