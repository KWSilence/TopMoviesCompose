package com.kwsilence.topmoviescompose.domain.usecase.movie

import com.kwsilence.topmoviescompose.domain.exception.detectInternetConnectionException
import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.MovieDetails
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository
import com.kwsilence.topmoviescompose.domain.util.InternetConnectionChecker

class GetMovieDetailsUseCase(
    private val repository: MovieRepository,
    private val connectionChecker: InternetConnectionChecker
) {
    suspend operator fun invoke(
        movieId: Int,
        imageWidth: Int? = null,
        loadMode: LoadMode = LoadMode.DEFAULT
    ): Result<MovieDetails> =
        repository.getMovieDetails(
            id = movieId,
            imageWidth = imageWidth,
            loadMode = loadMode
        ).detectInternetConnectionException(connectionChecker = connectionChecker)

    fun getFlow(movieId: Int) = repository.getMovieDetailsFlow(movieId)
}
