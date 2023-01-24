package com.kwsilence.topmoviescompose.data.repository

import com.kwsilence.topmoviescompose.data.repository.local.MovieLocalDataSource
import com.kwsilence.topmoviescompose.data.repository.remote.MovieRemoteDataSource
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduleEntity
import com.kwsilence.topmoviescompose.domain.model.LoadMode
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.model.MovieDetails
import com.kwsilence.topmoviescompose.domain.model.MoviesPage
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteRepository: MovieRemoteDataSource,
    private val localRepository: MovieLocalDataSource
) : MovieRepository {
    override suspend fun getPopularMovies(
        page: Int,
        imageWidth: Int?,
        loadMode: LoadMode
    ): Result<MoviesPage> {
        val forceLoad = loadMode == LoadMode.FORCE
        val localLoad = loadMode == LoadMode.LOCAL
        val hasLocalNextPage = localRepository.getLastPage() >= page
        return when (!localLoad && (forceLoad || !hasLocalNextPage)) {
            true -> {
                remoteRepository.getPopularMovies(page).processResult { moviesPage ->
                    val movieEntities = moviesPage.results.map { movie ->
                        movie.toEntity(
                            page = moviesPage.page,
                            posterUrl = remoteRepository.getImageUrl(
                                movie.posterPath, imageWidth
                            ), backdropUrl = remoteRepository.getImageUrl(
                                movie.backdropPath, imageWidth
                            )
                        )
                    }
                    if (forceLoad) localRepository.resetPages()
                    localRepository.addOrUpdateMovies(movieEntities)
                    val hasNextPage = moviesPage.run { page < total_pages }
                    val canLoadMore = hasNextPage && movieEntities.isNotEmpty()
                    localRepository.getPopularMovieListWithScheduleByPage(page) to canLoadMore
                }
            }
            false -> {
                val movieList = localRepository.getPopularMovieListWithScheduleByPage(page)
                val canLoadMore = (localLoad && hasLocalNextPage) || !localLoad
                Result.success(movieList to canLoadMore)
            }
        }.map { result -> result.first.toMoviePage(result.second) }
    }

    override suspend fun getMovieDetails(
        id: Int,
        imageWidth: Int?,
        loadMode: LoadMode
    ): Result<MovieDetails> {
        val localMovieEntity = localRepository.getScheduledMovieById(movieId = id)
        val localLoad = loadMode == LoadMode.LOCAL
        val forceLoad = loadMode == LoadMode.FORCE
        val movieNotFound = localMovieEntity == null
        val movieNotFilled = localMovieEntity != null && !localMovieEntity.movie.filled
        return when (!localLoad && (forceLoad || movieNotFound || movieNotFilled)) {
            true -> {
                remoteRepository.getMovieDetails(id).processResult { movieDetails ->
                    val movieEntity = movieDetails.toEntity(
                        posterUrl = remoteRepository.getImageUrl(
                            movieDetails.posterPath, imageWidth
                        ),
                        backdropUrl = remoteRepository.getImageUrl(
                            movieDetails.backdropPath, imageWidth
                        )
                    )
                    localRepository.run {
                        addOrUpdateMovies(listOf(movieEntity))
                        getScheduledMovieById(id)
                    } ?: throw Exception("Local movie not found")
                }
            }
            false -> {
                localMovieEntity?.let { movie ->
                    Result.success(movie)
                } ?: throw Exception("Local movie not found")
            }
        }.map { movieEntity -> movieEntity.toMovieDetails() }
    }

    override suspend fun scheduleMovieById(movieId: Int, time: Date) {
        localRepository.getMovieById(movieId) ?: throw Exception("Local movie not found")
        val scheduleEntity = ScheduleEntity(movieId, time)
        localRepository.upsertSchedule(scheduleEntity)
    }

    override suspend fun deleteScheduleByMovieId(movieId: Int) {
        localRepository.deleteScheduleByMovieId(movieId)
    }

    override suspend fun getScheduledMovies(): Result<List<Movie>> =
        Result.success(localRepository.getScheduledMovieList().map { it.toMovie() })

    override suspend fun deleteNonScheduledMovies() {
        localRepository.deleteNonScheduledMovies()
    }

    override suspend fun deleteAll() {
        localRepository.deleteAll()
    }

    override fun getPopularMoviesFlow(page: Int): Flow<List<Movie>> =
        localRepository.getLivePopularMovieListWithScheduleByPage(page).map { scheduledMovies ->
            scheduledMovies.map { scheduledMovie ->
                scheduledMovie.toMovie()
            }
        }

    override fun getMovieDetailsFlow(movieId: Int): Flow<MovieDetails?> =
        localRepository.getLiveScheduledMovieById(movieId).map { scheduledMovie ->
            scheduledMovie?.toMovieDetails()
        }

    override fun getScheduledMoviesFlow(): Flow<List<Movie>> =
        localRepository.getLiveScheduledMovieList().map { scheduledMovies ->
            scheduledMovies.map { scheduledMovie ->
                scheduledMovie.toMovie()
            }
        }

    private suspend fun <T, R> Result<T>.processResult(
        onSuccess: suspend (T) -> R, onFailure: suspend (Throwable) -> Result<R>
    ): Result<R> = when (isSuccess) {
        true -> getOrNull()?.let { result -> runCatching { onSuccess(result) } }
        false -> exceptionOrNull()?.let { onFailure(it) }
    } ?: Result.failure(Exception("Something wrong"))

    private suspend fun <T, R> Result<T>.processResult(
        onSuccess: suspend (T) -> R
    ) = processResult(onSuccess = onSuccess, onFailure = { Result.failure(it) })
}
