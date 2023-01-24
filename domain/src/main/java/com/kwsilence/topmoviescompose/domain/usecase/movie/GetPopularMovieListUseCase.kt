package com.kwsilence.topmoviescompose.domain.usecase.movie

import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.model.MoviesPage
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetPopularMovieListUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(
        page: Int,
        imageWidth: Int? = null,
        loadMode: LoadMode = LoadMode.DEFAULT
    ): Result<MoviesPage> =
        repository.getPopularMovies(page = page, imageWidth = imageWidth, loadMode = loadMode)

    fun getFlow(page: Int): Flow<List<Movie>> = repository.getPopularMoviesFlow(page)
}
