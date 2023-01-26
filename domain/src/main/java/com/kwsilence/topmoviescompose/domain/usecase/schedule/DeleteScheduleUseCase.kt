package com.kwsilence.topmoviescompose.domain.usecase.schedule

import com.kwsilence.topmoviescompose.domain.notification.NotificationScheduler
import com.kwsilence.topmoviescompose.domain.repository.MovieRepository

class DeleteScheduleUseCase(
    private val repository: MovieRepository,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(movieId: Int) {
        repository.deleteScheduleByMovieId(movieId)
        notificationScheduler.cancel(movieId)
    }
}
