package com.kwsilence.topmoviescompose.app

import android.app.Application
import com.kwsilence.topmoviescompose.BuildConfig
import com.kwsilence.topmoviescompose.di.appModule
import com.kwsilence.topmoviescompose.di.dataModule
import com.kwsilence.topmoviescompose.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

// todo on complete: move all strings and styles, mb add custom exception
// todo add on notification click show movie details screen
class TopMoviesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger()
            androidContext(this@TopMoviesApp)
            modules(appModule, dataModule, domainModule)
        }
    }
}
