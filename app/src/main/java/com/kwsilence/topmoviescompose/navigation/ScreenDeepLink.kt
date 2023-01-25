package com.kwsilence.topmoviescompose.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink

sealed class ScreenDeepLink(private val pattern: String) {
    object MovieDetails : ScreenDeepLink("$BASE_URI/movie/{id}") {
        fun buildUriString(movieId: Int): String = "$BASE_URI/movie/$movieId"
    }

    fun toNavDeepLink(): NavDeepLink = navDeepLink { uriPattern = pattern }

    private companion object {
        const val BASE_URI = "https://com.kwsilence.topmoviescompose"
    }
}
