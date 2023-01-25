package com.kwsilence.topmoviescompose.data.repository.local

import com.kwsilence.topmoviescompose.data.storage.db.MovieDao
import com.kwsilence.topmoviescompose.data.storage.entity.MovieEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduleEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduledMovie
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao
) : MovieLocalDataSource {
    override suspend fun addOrUpdateMovies(movieList: List<MovieEntity>) {
        movieDao.addOrUpdateMovies(
            movieList,
            onUpdateListener = { newMovie, oldMovie ->
                when (newMovie.filled) {
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
                }
            }
        )
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
        movieDao.deleteNonScheduledMoviesWithResetPages()
    }

    override suspend fun resetPages() {
        movieDao.resetPages()
    }

    override suspend fun deleteAll() {
        movieDao.deleteAll()
    }
}
