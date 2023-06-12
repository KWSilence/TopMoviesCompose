package com.kwsilence.topmoviescompose.data.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kwsilence.topmoviescompose.data.storage.entity.MovieEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduleEntity

@Database(entities = [MovieEntity::class, ScheduleEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        fun createDatabase(context: Context): MovieDatabase = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }
}
