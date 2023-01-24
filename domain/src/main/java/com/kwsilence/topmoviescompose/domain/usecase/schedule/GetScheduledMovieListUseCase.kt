package com.kwsilence.topmoviescompose.domain.usecase.schedule

import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetScheduledMovieListUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Result<List<Movie>> = repository.getScheduledMovies()
    fun getFlow(): Flow<List<Movie>> = repository.getScheduledMoviesFlow()
}
