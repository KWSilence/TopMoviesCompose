package com.kwsilence.topmoviescompose.domain.usecase.movie

import com.kwsilence.topmoviescompose.domain.exception.detectInternetConnectionException
import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.model.MoviesPage
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository
import com.kwsilence.topmoviescompose.domain.util.InternetConnectionChecker
import kotlinx.coroutines.flow.Flow

class GetPopularMovieListUseCase(
    private val repository: MovieRepository,
    private val connectionChecker: InternetConnectionChecker
) {
    suspend operator fun invoke(
        page: Int,
        imageWidth: Int? = null,
        loadMode: LoadMode = LoadMode.DEFAULT
    ): Result<MoviesPage> =
        repository.getPopularMovies(
            page = page,
            imageWidth = imageWidth,
            loadMode = loadMode
        ).detectInternetConnectionException(connectionChecker = connectionChecker)

    fun getFlow(page: Int): Flow<List<Movie>> = repository.getPopularMoviesFlow(page)
}
