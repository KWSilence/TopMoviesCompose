package com.kwsilence.topmoviescompose.data.repository.local

import com.kwsilence.topmoviescompose.data.storage.entity.MovieEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduleEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduledMovie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    suspend fun addOrUpdateMovies(movieList: List<MovieEntity>)
    suspend fun getMovieById(movieId: Int): MovieEntity?
    suspend fun getPopularMovieListByPage(page: Int): List<MovieEntity>

    suspend fun getLastPage(): Int

    suspend fun upsertSchedule(schedule: ScheduleEntity)
    suspend fun getScheduledMovieById(movieId: Int): ScheduledMovie?
    suspend fun getScheduledMovieList(): List<ScheduledMovie>
    suspend fun getPopularMovieListWithScheduleByPage(page: Int): List<ScheduledMovie>
    suspend fun deleteScheduleByMovieId(movieId: Int)

    fun getLivePopularMovieListWithScheduleByPage(page: Int): Flow<List<ScheduledMovie>>
    fun getLiveScheduledMovieById(movieId: Int): Flow<ScheduledMovie?>
    fun getLiveScheduledMovieList(): Flow<List<ScheduledMovie>>

    suspend fun deleteNonScheduledMovies()
    suspend fun resetPages()
    suspend fun deleteAll()
}
