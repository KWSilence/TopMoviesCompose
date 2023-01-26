package com.kwsilence.topmoviescompose.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kwsilence.topmoviescompose.domain.util.InternetConnectionChecker

class InternetConnectionCheckerImpl(context: Context) : InternetConnectionChecker {
    private val connectivityService = context.getSystemService(ConnectivityManager::class.java)

    override fun checkConnection(): Boolean =
        connectivityService.activeNetwork?.let { network ->
            connectivityService.getNetworkCapabilities(network)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
            }
        } ?: false
}
