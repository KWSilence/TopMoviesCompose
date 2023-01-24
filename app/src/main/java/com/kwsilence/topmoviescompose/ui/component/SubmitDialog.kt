package com.kwsilence.topmoviescompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SubmitDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onPositive: () -> Unit,
    onNegative: () -> Unit = {},
    positiveButtonName: String = "Yes",
    negativeButtonName: String = "No",
    titleTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    messageTextStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background, MaterialTheme.shapes.medium)
                .padding(10.dp)
        ) {
            Text(modifier = Modifier.fillMaxWidth(), text = title, style = titleTextStyle)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                text = message,
                style = messageTextStyle
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                SubmitButton(name = negativeButtonName, onClick = onNegative, onDismiss = onDismiss)
                SubmitButton(name = positiveButtonName, onClick = onPositive, onDismiss = onDismiss)
            }
        }
    }
}

@Composable
private fun RowScope.SubmitButton(name: String, onClick: () -> Unit, onDismiss: () -> Unit) {
    Text(
        modifier = Modifier
            .weight(1f)
            .clickable {
                onClick()
                onDismiss()
            }
            .padding(vertical = 10.dp),
        text = name,
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Center
    )
}
