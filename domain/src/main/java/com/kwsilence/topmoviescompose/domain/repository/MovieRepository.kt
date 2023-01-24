package com.kwsilence.topmoviescompose.domain.repository

import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.model.MovieDetails
import com.kwsilence.topmoviescompose.domain.model.MoviesPage
import java.util.Date
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(
        page: Int,
        imageWidth: Int? = null,
        loadMode: LoadMode = LoadMode.DEFAULT
    ): Result<MoviesPage>

    suspend fun getMovieDetails(
        id: Int,
        imageWidth: Int? = null,
        loadMode: LoadMode = LoadMode.DEFAULT
    ): Result<MovieDetails>

    suspend fun scheduleMovieById(movieId: Int, time: Date)
    suspend fun deleteScheduleByMovieId(movieId: Int)

    suspend fun getScheduledMovies(): Result<List<Movie>>

    suspend fun deleteNonScheduledMovies()
    suspend fun deleteAll()

    fun getPopularMoviesFlow(page: Int): Flow<List<Movie>>
    fun getMovieDetailsFlow(movieId: Int): Flow<MovieDetails?>
    fun getScheduledMoviesFlow(): Flow<List<Movie>>
}
