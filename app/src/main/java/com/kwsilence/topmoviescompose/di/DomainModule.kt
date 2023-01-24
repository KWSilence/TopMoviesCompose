package com.kwsilence.topmoviescompose.di

import com.kwsilence.topmoviescompose.domain.usecase.movie.GetMovieDetailsUseCase
import com.kwsilence.topmoviescompose.domain.usecase.movie.GetPopularMovieListUseCase
import com.kwsilence.topmoviescompose.domain.usecase.schedule.DeleteScheduleUseCase
import com.kwsilence.topmoviescompose.domain.usecase.schedule.GetScheduledMovieListUseCase
import com.kwsilence.topmoviescompose.domain.usecase.schedule.ScheduleMovieWatchingUseCase
import com.kwsilence.topmoviescompose.domain.validation.ScheduleTimeValidator
import com.kwsilence.topmoviescompose.domain.validation.ScheduleTimeValidatorImpl
import org.koin.dsl.module

val domainModule = module {
    factory { GetPopularMovieListUseCase(get()) }
    factory { GetMovieDetailsUseCase(get()) }
    factory { ScheduleMovieWatchingUseCase(get(), get(), get()) }
    factory { GetScheduledMovieListUseCase(get()) }
    factory { DeleteScheduleUseCase(get(), get()) }

    factory<ScheduleTimeValidator> { ScheduleTimeValidatorImpl() }
}
