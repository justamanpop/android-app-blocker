package com.example.appblocker.ui.screens.addBlockSetScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun AddBlockSetScreen(viewModel: AddBlockSetScreenViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var nameTextFieldValue by remember { mutableStateOf("") }
    Scaffold() {
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(nameTextFieldValue, {
                nameTextFieldValue = it
            })
            Button({
                keyboardController?.hide()
                viewModel.createBlockSet(nameTextFieldValue)
            }) {
                Text("Create block set")
            }
        }
    }
}
