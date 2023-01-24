package com.kwsilence.topmoviescompose.domain.validation

import java.util.Date

class ScheduleTimeValidatorImpl : ScheduleTimeValidator {
    override fun validate(time: Date): ValidationResult {
        val currentTime = Date()
        return when (time.before(currentTime)) {
            true -> ValidationResult(isValid = false, error = "Schedule time in the past")
            false -> ValidationResult(isValid = true)
        }
    }
}