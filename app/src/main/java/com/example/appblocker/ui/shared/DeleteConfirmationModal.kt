package com.example.appblocker.ui.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appblocker.ui.theme.AccentDark
import com.example.appblocker.ui.theme.AccentLight
import com.example.appblocker.ui.theme.Border
import com.example.appblocker.ui.theme.CardElevated
import com.example.appblocker.ui.theme.OnPrimary
import com.example.appblocker.ui.theme.TextSecondary

@Composable
fun DeleteConfirmationModal(
    nameOfDeletionItem: String,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit
) {
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
                colors = ButtonDefaults.textButtonColors(
                    containerColor = AccentLight,
                    contentColor = OnPrimary
                ),
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = AccentDark,
                    contentColor = TextSecondary
                ),
                border = BorderStroke(1.dp, Border)
            ) {
                Text("Cancel")
            }
        }
    )
}