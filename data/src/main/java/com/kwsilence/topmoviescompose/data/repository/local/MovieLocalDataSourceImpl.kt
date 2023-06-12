package com.kwsilence.topmoviescompose.data.repository.local

import androidx.room.withTransaction
import com.kwsilence.topmoviescompose.data.storage.db.MovieDatabase
import com.kwsilence.topmoviescompose.data.storage.entity.MovieEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduleEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduledMovie
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSourceImpl(
    private val movieDatabase: MovieDatabase
) : MovieLocalDataSource {
    private val movieDao get() = movieDatabase.movieDao

    override suspend fun addOrUpdateMovies(movieList: List<MovieEntity>) {
        movieDatabase.withTransaction {
            movieList.forEach { newMovie ->
                when (val oldMovie = movieDao.getMovieById(newMovie.id)) {
                    null -> movieDao.addMovie(newMovie)
                    else -> when (newMovie.filled) {
                        true -> newMovie.copy(page = newMovie.page ?: oldMovie.page)
                        false -> newMovie.run {
                            oldMovie.copy(
                                adult = adult,
                                backdropPath = backdropPath,
                                originalLang = originalLang,
                                originalTitle = originalTitle,
                                overview = overview,
                                popularity = popularity,
                                posterPath = posterPath,
                                releaseDate = releaseDate,
                                title = title,
                                voteAverage = voteAverage,
                                voteCount = voteCount,
                                page = page ?: oldMovie.page
                            )
                        }
                    }.let { movieToUpdate -> movieDao.updateMovie(movieToUpdate) }
                }
            }
        }
    }

    override suspend fun getMovieById(movieId: Int): MovieEntity? =
        movieDao.getMovieById(movieId)

    override suspend fun getPopularMovieListByPage(page: Int): List<MovieEntity> =
        movieDao.getPopularMovieListByPage(page)

    override suspend fun getLastPage(): Int =
        movieDao.getLastPage() ?: 0

    override suspend fun upsertSchedule(schedule: ScheduleEntity) {
        movieDao.upsertSchedule(schedule)
    }

    override suspend fun getScheduledMovieById(movieId: Int): ScheduledMovie? =
        movieDao.getScheduledMovieById(movieId)

    override suspend fun getScheduledMovieList(): List<ScheduledMovie> =
        movieDao.getScheduledMovieList()

    override suspend fun getPopularMovieListWithScheduleByPage(page: Int): List<ScheduledMovie> =
        movieDao.getPopularMovieListWithScheduleByPage(page)

    override suspend fun deleteScheduleByMovieId(movieId: Int) {
        movieDao.deleteScheduleByMovieId(movieId)
    }

    override fun getLivePopularMovieListWithScheduleByPage(page: Int): Flow<List<ScheduledMovie>> =
        movieDao.getLivePopularMovieListWithScheduleByPage(page)

    override fun getLiveScheduledMovieById(movieId: Int): Flow<ScheduledMovie?> =
        movieDao.getLiveScheduledMovieById(movieId)

    override fun getLiveScheduledMovieList(): Flow<List<ScheduledMovie>> =
        movieDao.getLiveScheduledMovieList()

    override suspend fun deleteNonScheduledMovies() {
        movieDatabase.withTransaction {
            movieDao.deleteNonScheduledMovies()
            movieDao.resetPages()
        }
    }

    override suspend fun resetPages() {
        movieDao.resetPages()
    }

    override suspend fun deleteAll() {
        movieDao.deleteAll()
    }
}
