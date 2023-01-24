package com.kwsilence.topmoviescompose.data.storage.entity

import androidx.room.Embedded

data class ScheduledMovie(
    @Embedded val movie: MovieEntity,
//    @Relation(parentColumn = "id", entityColumn = "movie_id")
    @Embedded val schedule: ScheduleEntity?
)
