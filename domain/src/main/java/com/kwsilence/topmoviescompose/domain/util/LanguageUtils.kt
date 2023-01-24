package com.kwsilence.topmoviescompose.domain.util

import java.util.Locale

object LanguageUtils {
    val locale: Locale get() = Locale.US
//    val language: String = locale.toLanguageTag()
    fun getName(language: String): String = Locale(language).getDisplayName(locale)
}
