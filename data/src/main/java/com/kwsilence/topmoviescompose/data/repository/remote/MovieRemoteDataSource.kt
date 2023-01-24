package com.kwsilence.topmoviescompose.data.repository.remote

import com.kwsilence.topmoviescompose.data.api.MovieRetrofit
import com.kwsilence.topmoviescompose.data.api.model.ApiMovieDetails
import com.kwsilence.topmoviescompose.data.api.model.ApiMoviesPage

interface MovieRemoteDataSource {
    suspend fun getPopularMovies(
        page: Int,
        api_key: String = MovieRetrofit.apiKey
    ): Result<ApiMoviesPage>

    suspend fun getMovieDetails(
        id: Int,
        api_key: String = MovieRetrofit.apiKey
    ): Result<ApiMovieDetails>

    suspend fun getImageUrl(path: String?, width: Int?): String?
}
