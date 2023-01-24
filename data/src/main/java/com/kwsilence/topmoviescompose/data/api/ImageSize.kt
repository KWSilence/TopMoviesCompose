package com.kwsilence.topmoviescompose.data.api

sealed class ImageSize(val path: String) {
    object Original : ImageSize("original/")
    data class Custom(val width: Int) : ImageSize("w$width/")

    companion object {
        fun getSize(width: Int?): ImageSize = width?.let { Custom(it) } ?: Original
    }
}
