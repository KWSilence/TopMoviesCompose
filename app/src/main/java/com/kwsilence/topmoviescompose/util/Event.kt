package com.kwsilence.topmoviescompose.util

class Event<out T>(private val value: T?) {
    private var hasBeenHandled = false

    val content: T?
        get() = when (hasBeenHandled) {
            true -> null
            false -> {
                hasBeenHandled = true
                value
            }
        }
}

fun <T> T?.toEvent(): Event<T> = Event(this)
