package com.kwsilence.topmoviescompose.data.storage.db.listener

import com.kwsilence.topmoviescompose.data.storage.entity.MovieEntity

fun interface OnMovieUpdateListener {
    fun onUpdate(newMovie: MovieEntity, oldMovie: MovieEntity): MovieEntity
}
