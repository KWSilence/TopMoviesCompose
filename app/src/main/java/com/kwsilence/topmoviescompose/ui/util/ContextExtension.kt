package com.kwsilence.topmoviescompose.ui.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let { msg -> Toast.makeText(this, msg, length).show() }
}
