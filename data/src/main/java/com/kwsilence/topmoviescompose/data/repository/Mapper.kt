package com.kwsilence.topmoviescompose.data.repository

import com.kwsilence.topmoviescompose.data.api.model.ApiGenre
import com.kwsilence.topmoviescompose.data.api.model.ApiMovie
import com.kwsilence.topmoviescompose.data.api.model.ApiMovieDetails
import com.kwsilence.topmoviescompose.data.storage.entity.MovieEntity
import com.kwsilence.topmoviescompose.data.storage.entity.ScheduledMovie
import com.kwsilence.topmoviescompose.domain.model.Genre
import com.kwsilence.topmoviescompose.domain.model.Movie
import com.kwsilence.topmoviescompose.domain.model.MovieDetails
import com.kwsilence.topmoviescompose.domain.model.MoviesPage
import com.kwsilence.topmoviescompose.domain.util.LanguageUtils
import java.text.SimpleDateFormat
import java.util.Date

private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", LanguageUtils.locale)
private fun String?.convertApiDate(): Date? =
    if (this.isNullOrBlank()) null else apiDateFormat.parse(this)

private val genreRegex = """\[(\d*)] ([^,]*)""".toRegex()
private fun List<Genre>.toEntityString(): String = joinToString { "[${it.id}] ${it.name}" }
private fun String.toGenreList(): List<Genre> = genreRegex.findAll(this).map { match ->
    match.groups.run { Genre(get(1)?.value?.toInt(), get(2)?.value ?: "None") }
}.toList()

fun List<ApiGenre>.toGenreList(): List<Genre> = map { apiGenre ->
    Genre(apiGenre.id, apiGenre.name)
}

fun ApiMovie.toEntity(
    page: Int? = null,
    backdropUrl: String? = null,
    posterUrl: String? = null
): MovieEntity =
    MovieEntity(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        originalLang = LanguageUtils.getName(language = originalLang),
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage / 10f,
        voteCount = voteCount,
        filled = false,
        page = page,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl
    )

fun ApiMovieDetails.toEntity(
    page: Int? = null,
    backdropUrl: String? = null,
    posterUrl: String? = null
): MovieEntity =
    MovieEntity(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        originalLang = LanguageUtils.getName(language = originalLang),
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage / 10f,
        voteCount = voteCount,
        homepage = homepage,
        status = status,
        tagline = tagline,
        revenue = revenue,
        runtime = runtime,
        budget = budget,
        genres = genres.toGenreList().toEntityString(),
        filled = true,
        page = page,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl
    )

fun MovieEntity.toMovieDetails(): MovieDetails =
    MovieDetails(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        originalLang = originalLang,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate.convertApiDate(),
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount,
        homepage = homepage,
        status = status,
        tagline = tagline,
        revenue = revenue,
        runtime = runtime,
        budget = budget,
        genres = genres?.toGenreList(),
        backdropUrl = backdropUrl,
        posterUrl = posterUrl
    )

fun MovieEntity.toMovie(): Movie =
    Movie(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        originalLang = originalLang,
        originalTitle = originalTitle,
        overview = overview ?: "",
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate.convertApiDate(),
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl
    )

fun ScheduledMovie.toMovie() = movie.toMovie().copy(scheduled = schedule?.time)
fun ScheduledMovie.toMovieDetails() = movie.toMovieDetails().copy(scheduled = schedule?.time)
fun List<ScheduledMovie>.toMoviePage(canLoadMore: Boolean) =
    MoviesPage(movieList = map { movie -> movie.toMovie() }, canLoadMore = canLoadMore)
