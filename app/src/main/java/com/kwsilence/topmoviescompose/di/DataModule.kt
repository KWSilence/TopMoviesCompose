package com.kwsilence.topmoviescompose.di

import com.kwsilence.topmoviescompose.data.api.MovieRetrofit
import com.kwsilence.topmoviescompose.data.repository.MovieRepositoryImpl
import com.kwsilence.topmoviescompose.data.repository.local.MovieLocalDataSource
import com.kwsilence.topmoviescompose.data.repository.local.MovieLocalDataSourceImpl
import com.kwsilence.topmoviescompose.data.repository.remote.MovieRemoteDataSource
import com.kwsilence.topmoviescompose.data.repository.remote.MovieRemoteDataSourceImpl
import com.kwsilence.topmoviescompose.data.storage.db.MovieDao
import com.kwsilence.topmoviescompose.data.storage.db.MovieDatabase
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository
import org.koin.dsl.module

val dataModule = module {
    single { MovieRetrofit.movieApi }
    single { MovieDatabase.createDatabase(get()) }
    factory<MovieRemoteDataSource> { MovieRemoteDataSourceImpl(get()) }
    factory<MovieLocalDataSource> {
        val movieDatabase: MovieDatabase = get()
        val movieDao: MovieDao = movieDatabase.movieDao()
        MovieLocalDataSourceImpl(movieDao)
    }
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
}
