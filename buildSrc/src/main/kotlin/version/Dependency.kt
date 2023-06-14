package version

object Dependency {
    object Core {
        const val androidKtx = "androidx.core:core-ktx:1.10.1"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1"
    }

    object Lifecycle {
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.Dep.lifecycle}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.Dep.lifecycle}"
        const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Version.Dep.lifecycle}"
    }

    object Compose {
        const val activity = "androidx.activity:activity-compose:${Version.Dep.activityCompose}"
        const val ui = "androidx.compose.ui:ui:${Version.Dep.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Version.Dep.compose}"
        const val material = "androidx.compose.material:material:${Version.Dep.compose}"
    }

    object Navigation {
        const val compose = "androidx.navigation:navigation-compose:${Version.Dep.composeNavigation}"
    }

    object Coil {
        const val compose = "io.coil-kt:coil-compose:${Version.Dep.coil}"
    }

    object Log {
        const val timber = "com.jakewharton.timber:timber:${Version.Dep.timber}"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${Version.Dep.koin}"
        const val androidCompose = "io.insert-koin:koin-androidx-compose:${Version.Dep.koinAndroidCompose}"
    }

    object OkHttp {
        const val core = "com.squareup.okhttp3:okhttp:${Version.Dep.okHttp}"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.Dep.okHttp}"
    }

    object Retrofit {
        const val core = "com.squareup.retrofit2:retrofit:${Version.Dep.retrofit}"
        const val converterGson = "com.squareup.retrofit2:converter-gson:${Version.Dep.retrofit}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Version.Dep.room}"
        const val compiler = "androidx.room:room-compiler:${Version.Dep.room}"
        const val ktx = "androidx.room:room-ktx:${Version.Dep.room}"
        const val testing = "androidx.room:room-testing:${Version.Dep.room}"
    }

    object Accompanist {
        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:0.30.1"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val extJunit = "androidx.test.ext:junit:1.1.5"
        const val espresso = "androidx.test.espresso:espresso-core:3.5.1"
        const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Version.Dep.compose}"
    }

    object Debug {
        const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Version.Dep.compose}"
        const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Version.Dep.compose}"
    }
}