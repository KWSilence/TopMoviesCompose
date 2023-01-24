package com.kwsilence.topmoviescompose.domain.validation

import java.util.Date

fun interface ScheduleTimeValidator {
    fun validate(time: Date): ValidationResult
}