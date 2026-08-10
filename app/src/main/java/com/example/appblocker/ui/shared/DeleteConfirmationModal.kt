package com.example.appblocker.ui.shared

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteConfirmationModal(nameOfDeletionItem: String, onDelete: () -> Unit, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete block set $nameOfDeletionItem?") },
        confirmButton = {
            Button(
                onClick = {
                    onDelete()
                    onDismissRequest()
                },
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color(201, 9, 35, 255),
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color(186, 22, 39),
                ),
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonColors(
                    containerColor = Color(52, 161, 235),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Gray
                ),
            ) {
                Text("Cancel")
            }
        }
    )
}