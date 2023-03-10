package com.kwsilence.topmoviescompose.data.repository.remote

import com.kwsilence.topmoviescompose.data.api.ImageSize
import com.kwsilence.topmoviescompose.data.api.MovieApi
import com.kwsilence.topmoviescompose.data.api.MovieRetrofit
import com.kwsilence.topmoviescompose.data.api.model.ApiMovieDetails
import com.kwsilence.topmoviescompose.data.api.model.ApiMoviesPage
import com.kwsilence.topmoviescompose.data.exception.HostConnectionException
import com.kwsilence.topmoviescompose.data.exception.TimeoutException
import com.kwsilence.topmoviescompose.data.exception.UnknownException
import com.kwsilence.topmoviescompose.domain.exception.RemoteSourceException
import retrofit2.Call
import retrofit2.awaitResponse
import java.net.ConnectException
import java.net.SocketTimeoutException

class MovieRemoteDataSourceImpl(private val api: MovieApi) : MovieRemoteDataSource {
    private companion object {
        const val MAX_PAGE_COUNT = 500
    }

    override suspend fun getPopularMovies(page: Int, api_key: String): Result<ApiMoviesPage> {
        return when (page > MAX_PAGE_COUNT) {
            true -> Result.success(ApiMoviesPage(page, emptyList(), MAX_PAGE_COUNT, null))
            false -> api.getPopularMovies(page, api_key).awaitResult()
                .map { it.copy(total_pages = MAX_PAGE_COUNT) }
        }
    }

    override suspend fun getMovieDetails(id: Int, api_key: String): Result<ApiMovieDetails> =
        api.getMovieDetails(id, api_key).awaitResult()

    override suspend fun getImageUrl(path: String?, width: Int?): String? {
        path ?: return null
        val size = ImageSize.getSize(width).path
        return MovieRetrofit.baseImageUrl + size + path
    }

    private suspend fun <T> Call<T>.awaitResult(): Result<T> =
        runCatching { awaitResponse() }.fold(
            onSuccess = { response ->
                when (response.isSuccessful) {
                    true -> response.body()?.let { Result.success(it) }
                    false -> Result.failure(Exception(response.message()))
                } ?: Result.failure(UnknownException())
            },
            onFailure = { exception ->
                when (exception) {
                    is SocketTimeoutException -> TimeoutException(exception)
                    is ConnectException -> HostConnectionException(exception)
                    else -> RemoteSourceException(exception)
                }.let { Result.failure(it) }
            }
        )
}
