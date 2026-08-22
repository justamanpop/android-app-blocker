package com.example.appblocker.ui.screens.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppSettingsPreferences

@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 8.dp),
    ) {
        val settings by viewModel.settingsFlow.collectAsStateWithLifecycle(
            initialValue = AppSettingsPreferences(0,0)
        )
        var blockSetLockDurationInSeconds by remember { mutableIntStateOf(settings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds) }
        var blockListAppLockDurationInSeconds by remember { mutableIntStateOf(settings.appBlockListLockDurationAfterAddToBlockListInSeconds) }

        Text(
            text = "Settings",
            fontSize = 32.sp,
            lineHeight = 32.sp,
        )

        Spacer(Modifier.height(8.dp))
        Text("Field name")
        Text("sub text explaining field")
        OutlinedTextField(
            blockSetLockDurationInSeconds.toString(), {
                blockSetLockDurationInSeconds = it.toInt()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )

        Spacer(Modifier.height(8.dp))
        Text("Field name 2")
        Text("sub text explaining field 2")
        OutlinedTextField(
            blockListAppLockDurationInSeconds.toString(), {
                blockListAppLockDurationInSeconds = it.toInt()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )
    }
}
