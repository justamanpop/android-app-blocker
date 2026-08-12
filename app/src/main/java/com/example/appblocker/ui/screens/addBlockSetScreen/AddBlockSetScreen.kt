package com.example.appblocker.ui.screens.addBlockSetScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AddBlockSetScreen(viewModel: AddBlockSetScreenViewModel, onAddBlockSet: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var nameTextFieldValue by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White,
                    actionColor = Color.Yellow,
                    snackbarData = data
                )
            }
        }
    ) {
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(start = 12.dp)) {
            OutlinedTextField(nameTextFieldValue, {
                nameTextFieldValue = it
            })
            Button({
                val blockSetToCreateName =nameTextFieldValue

                keyboardController?.hide()
                viewModel.createBlockSet(blockSetToCreateName)
                nameTextFieldValue = ""
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = "Block set $blockSetToCreateName created!")
                }
                onAddBlockSet()
            }) {
                Text("Create block set")
            }
        }
    }
}
