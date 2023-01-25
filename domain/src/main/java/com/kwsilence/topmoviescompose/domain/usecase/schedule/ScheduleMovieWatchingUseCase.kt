package com.kwsilence.topmoviescompose.domain.usecase.schedule

import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.notification.NotificationScheduler
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository
import com.kwsilence.topmoviescompose.domain.validation.ScheduleTimeValidator
import java.util.Date

class ScheduleMovieWatchingUseCase(
    private val repository: MovieRepository,
    private val notificationScheduler: NotificationScheduler,
    private val timeValidator: ScheduleTimeValidator
) {
    suspend operator fun invoke(movie: Movie?, time: Date, uriString: String? = null) {
        movie ?: throw Exception("Movie not found")
        val validationResult = timeValidator.validate(time)
        if (!validationResult.isValid) throw Exception(validationResult.error)
        repository.scheduleMovieById(movie.id, time)
        notificationScheduler.schedule(
            id = movie.id,
            date = time,
            title = "Time to watch movie!",
            text = movie.title,
            uriString = uriString
        )
    }
}
