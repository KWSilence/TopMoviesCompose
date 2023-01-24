package com.kwsilence.topmoviescompose.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiMovie(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val id: Int,
    @SerializedName("original_language")
    val originalLang: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int
)
