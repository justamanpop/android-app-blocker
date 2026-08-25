package com.example.appblocker.ui.screens.settingsScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppSettingsPreferences
import com.example.appblocker.ui.shared.SecondaryButton
import com.example.appblocker.ui.theme.TextPrimary
import com.example.appblocker.ui.theme.TextSecondary
import com.example.appblocker.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel, onGoBack: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(top = 8.dp, start = 16.dp, end = 8.dp),
        ) {
            val settings by viewModel.settingsFlow.collectAsStateWithLifecycle(
                initialValue = AppSettingsPreferences(0, 0)
            )

            Text(
                text = "Settings",
                fontSize = 32.sp,
                lineHeight = 32.sp,
            )

            Spacer(Modifier.height(8.dp))
            Text("Block set lock duration", color = TextPrimary, style = Typography.titleMedium)
            Text(
                "Amount of time block set is locked (unchangeable) for after. Meant to stop user from simply editing/deleting a block set when they have an urge to use a blocked app",
                color = TextSecondary,
                style = Typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))

            var blockSetLockDurationDays by remember { mutableStateOf("0") }
            var blockSetLockDurationHours by remember { mutableStateOf("0") }
            var blockSetLockDurationMinutes by remember { mutableStateOf("0") }

            LaunchedEffect(settings) {
                val fv = viewModel.getBlockSetLockFieldValuesFromStoredSettings(settings)
                blockSetLockDurationDays = fv.days
                blockSetLockDurationHours = fv.hours
                blockSetLockDurationMinutes = fv.minutes
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Days")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        blockSetLockDurationDays,
                        { newVal ->
                            //don't allow decimals, negative signs
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }
                            blockSetLockDurationDays = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Hours (0-23)")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        blockSetLockDurationHours,
                        { newVal ->
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }

                            val newValInt = newVal.toIntOrNull()
                            if (newValInt != null && newValInt > 23) {
                                return@OutlinedTextField
                            }

                            blockSetLockDurationHours = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Minutes (0-59)")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        blockSetLockDurationMinutes,
                        { newVal ->
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }

                            val newValInt = newVal.toIntOrNull()
                            if (newValInt != null && newValInt > 59) {
                                return@OutlinedTextField
                            }

                            blockSetLockDurationMinutes = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Blocked app lock duration", color = TextPrimary, style = Typography.titleMedium)
            Text(
                "Amount of time a blocked app cannot be removed from a block list after being added. Meant to stop user from simply removing the app when an urge to use it hits",
                color = TextSecondary,
                style = Typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            var blockedAppLockDurationDays by remember { mutableStateOf("0") }
            var blockedAppLockDurationHours by remember { mutableStateOf("0") }
            var blockedAppLockDurationMinutes by remember { mutableStateOf("0") }

            LaunchedEffect(settings) {
                val fv = viewModel.getBlockListLockFieldValuesFromStoredSettings(settings)
                blockedAppLockDurationDays = fv.days
                blockedAppLockDurationHours = fv.hours
                blockedAppLockDurationMinutes = fv.minutes
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Days")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        blockedAppLockDurationDays,
                        { newVal ->
                            //don't allow decimals, negative signs
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }
                            blockedAppLockDurationDays = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Hours (0-23)")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        blockedAppLockDurationHours,
                        { newVal ->
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }

                            val newValInt = newVal.toIntOrNull()
                            if (newValInt != null && newValInt > 23) {
                                return@OutlinedTextField
                            }

                            blockedAppLockDurationHours = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Minutes (0-59)")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        blockedAppLockDurationMinutes,
                        { newVal ->
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }

                            val newValInt = newVal.toIntOrNull()
                            if (newValInt != null && newValInt > 59) {
                                return@OutlinedTextField
                            }

                            blockedAppLockDurationMinutes = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Row {
                Button({
                    keyboardController?.hide()
                    val blockDuration = viewModel.getDurationFromFieldValues(
                        blockSetLockDurationDays,
                        blockSetLockDurationHours,
                        blockSetLockDurationMinutes,
                        blockedAppLockDurationDays,
                        blockedAppLockDurationHours,
                        blockedAppLockDurationMinutes
                    )
                    Log.d("debugSetting", "block duration to save is $blockDuration")
                    viewModel.updateSettings(blockDuration)
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(message = "Settings updated!")
                    }
                }) {
                    Text("Save settings")
                }
                Spacer(Modifier.width(8.dp))
                SecondaryButton(onClick = {
                    keyboardController?.hide()
                    onGoBack()
                }) {
                    Text("Go back")
                }
            }

        }
    }
}
