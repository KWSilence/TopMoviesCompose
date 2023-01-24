package com.kwsilence.topmoviescompose.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)
