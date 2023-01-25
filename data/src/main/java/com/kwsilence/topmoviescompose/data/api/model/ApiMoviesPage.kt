package com.kwsilence.topmoviescompose.data.api.model

data class ApiMoviesPage(
    val page: Int,
    val results: List<ApiMovie>,
    val total_pages: Int,
    val total_results: Int?
)
