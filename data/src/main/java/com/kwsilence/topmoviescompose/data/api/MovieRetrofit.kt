package com.kwsilence.topmoviescompose.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieRetrofit {
    private const val baseApiUrl = "https://api.themoviedb.org/3/"
    const val baseImageUrl = "https://image.tmdb.org/t/p/"
    const val apiKey = "e0621399003b72fad276dc4972cbed5f"

    private val loggingInterceptor
        get() = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val httpClient
        get() = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    private val movieRetrofit
        get() = Retrofit.Builder()
            .baseUrl(baseApiUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val movieApi: MovieApi get() = movieRetrofit.create(MovieApi::class.java)
}
