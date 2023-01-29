package com.kwsilence.topmoviescompose.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.kwsilence.topmoviescompose.domain.notification.NotificationScheduler
import com.kwsilence.topmoviescompose.domain.util.InternetConnectionChecker
import com.kwsilence.topmoviescompose.feature.movie.details.MovieDetailsViewModel
import com.kwsilence.topmoviescompose.feature.movie.list.MovieListViewModel
import com.kwsilence.topmoviescompose.feature.schedule.list.ScheduleListViewModel
import com.kwsilence.topmoviescompose.feature.schedule.time.ScheduleTimeViewModel
import com.kwsilence.topmoviescompose.notification.NotificationSchedulerImpl
import com.kwsilence.topmoviescompose.util.InternetConnectionCheckerImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MovieListViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { ScheduleTimeViewModel(get(), get(), get()) }
    viewModel { ScheduleListViewModel(get(), get()) }

    factory<NotificationScheduler> { NotificationSchedulerImpl(get()) }
    factory<InternetConnectionChecker> { InternetConnectionCheckerImpl(get()) }

    single {
        val context: Context = get()
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.2)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("movie_poster_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }
}
