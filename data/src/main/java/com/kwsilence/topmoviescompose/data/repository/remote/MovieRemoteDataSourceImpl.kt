package com.kwsilence.topmoviescompose.data.repository.remote

import com.kwsilence.topmoviescompose.data.api.ImageSize
import com.kwsilence.topmoviescompose.data.api.MovieApi
import com.kwsilence.topmoviescompose.data.api.MovieRetrofit
import com.kwsilence.topmoviescompose.data.api.model.ApiMovieDetails
import com.kwsilence.topmoviescompose.data.api.model.ApiMoviesPage
import retrofit2.Response
import retrofit2.awaitResponse

class MovieRemoteDataSourceImpl(private val api: MovieApi) : MovieRemoteDataSource {
    private companion object {
        const val MAX_PAGE_COUNT = 500
    }

    override suspend fun getPopularMovies(page: Int, api_key: String): Result<ApiMoviesPage> {
        return when (page > MAX_PAGE_COUNT) {
            true -> Result.success(ApiMoviesPage(page, emptyList(), MAX_PAGE_COUNT, null))
            false -> api.getPopularMovies(page, api_key).awaitResponse().getResultFromResponse()
                .map { it.copy(total_pages = MAX_PAGE_COUNT) }
        }
    }

    override suspend fun getMovieDetails(id: Int, api_key: String): Result<ApiMovieDetails> =
        api.getMovieDetails(id, api_key).awaitResponse().getResultFromResponse()

    override suspend fun getImageUrl(path: String?, width: Int?): String? {
        path ?: return null
        val size = ImageSize.getSize(width).path
        return MovieRetrofit.baseImageUrl + size + path
    }

    private fun <T> Response<T>.getResultFromResponse(): Result<T> = when (isSuccessful) {
        true -> body()?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Something wrong"))
        false -> Result.failure(Exception(message()))
    }
}
