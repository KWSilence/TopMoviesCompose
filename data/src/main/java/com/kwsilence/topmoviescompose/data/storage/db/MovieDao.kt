package com.kwsilence.topmoviescompose.data.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.kwsilence.topmoviescompose.data.storage.db.listener.OnMovieUpdateListener
import com.kwsilence.topmoviescompose.data.storage.entity.MovieEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduleEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduledMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun addMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Transaction
    suspend fun addOrUpdateMovies(
        movieList: List<MovieEntity>,
        onUpdateListener: OnMovieUpdateListener = OnMovieUpdateListener { newMovie, _ -> newMovie }
    ) {
        movieList.forEach { newMovie ->
            when (val oldMovie = getMovieById(newMovie.id)) {
                null -> addMovie(newMovie)
                else -> updateMovie(onUpdateListener.onUpdate(newMovie, oldMovie))
            }
        }
    }

    @Query("select * from movie_table where id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("select * from movie_table where page = :page order by popularity desc")
    suspend fun getPopularMovieListByPage(page: Int): List<MovieEntity>

    @Upsert
    suspend fun upsertSchedule(scheduleEntity: ScheduleEntity)

//    @Transaction
    @Query(
        "select * from movie_table left join schedule_table " +
            "on movie_table.id = schedule_table.movie_id " +
            "where id = :id"
    )
    suspend fun getScheduledMovieById(id: Int): ScheduledMovie?

//    @Transaction
    @Query(
        "select * from movie_table left join schedule_table " +
            "on movie_table.id = schedule_table.movie_id " +
            "where id = :id"
    )
    fun getLiveScheduledMovieById(id: Int): Flow<ScheduledMovie?>

//    @Transaction
    @Query(
        "select * from movie_table inner join schedule_table " +
            "on movie_table.id = schedule_table.movie_id " +
            "order by schedule_table.time desc"
    )
    suspend fun getScheduledMovieList(): List<ScheduledMovie>

//    @Transaction
    @Query(
        "select * from movie_table inner join schedule_table " +
            "on movie_table.id = schedule_table.movie_id " +
            "order by schedule_table.time asc"
    )
    fun getLiveScheduledMovieList(): Flow<List<ScheduledMovie>>

//    @Transaction
    @Query(
        "select * from movie_table left join schedule_table " +
            "on movie_table.id = schedule_table.movie_id " +
            "where page = :page " +
            "order by popularity desc"
    )
    suspend fun getPopularMovieListWithScheduleByPage(page: Int): List<ScheduledMovie>

//    @Transaction
    @Query(
        "select * from movie_table left join schedule_table " +
            "on movie_table.id = schedule_table.movie_id " +
            "where page > 0 and page <= :page " +
            "order by page asc, popularity desc"
    )
    fun getLivePopularMovieListWithScheduleByPage(page: Int): Flow<List<ScheduledMovie>>

    @Query("delete from schedule_table where movie_id = :id")
    suspend fun deleteScheduleByMovieId(id: Int)

    @Query("select max(page) from movie_table")
    suspend fun getLastPage(): Int?

    @Query(
        "delete from movie_table where id not in " +
            "(select movie_id from schedule_table where time is not null)"
    )
    suspend fun deleteNonScheduledMovies()

    @Query("update movie_table set page = null")
    suspend fun resetPages()

    @Transaction
    suspend fun deleteNonScheduledMoviesWithResetPages() {
        deleteNonScheduledMovies()
        resetPages()
    }

    @Query("delete from movie_table")
    suspend fun deleteAll()
}
