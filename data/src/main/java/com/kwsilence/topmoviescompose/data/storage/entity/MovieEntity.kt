package com.kwsilence.topmoviescompose.data.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "original_lang") val originalLang: String,
    @ColumnInfo(name = "original_title") val originalTitle: String,
    val overview: String?,
    val popularity: Float,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    val title: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Float,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
    @ColumnInfo(name = "backdrop_url") val backdropUrl: String? = null,
    @ColumnInfo(name = "poster_url") val posterUrl: String? = null,

//    Detail info
    val homepage: String? = null,
    val status: String? = null,
    val tagline: String? = null,
    val revenue: Long? = null,
    val runtime: Int? = null,
    val budget: Long? = null,
    val genres: String? = null,

//    Support info
    val filled: Boolean = false,
    val page: Int? = null
)
