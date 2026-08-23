package com.example.appblocker.ui.screens.settingsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppSettingsPreferences
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel) {
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
                .padding(top = 8.dp, start = 16.dp),
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
            Text("Block set lock duration")
            Text("Amount of time block set is locked (unchangeable) for after. Meant to stop user from simply editing/deleting a block set when they have an urge to use a blocked app")

            var blockSetLockDurationDays by remember { mutableStateOf(settings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds) }
            var blockSetLockDurationDaysErrorMessage by remember { mutableStateOf<String?>(null) }

            var blockSetLockDurationHours by remember { mutableIntStateOf(settings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds) }
            var blockSetLockDurationHoursErrorMessage by remember { mutableStateOf<String?>(null) }

            var blockSetLockDurationMinutes by remember { mutableIntStateOf(settings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds) }
            var blockSetLockDurationMinutesErrorMessage by remember { mutableStateOf<String?>(null) }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedTextField(
                    blockSetLockDurationDays.toString(),
                    { newVal ->
                        //don't allow decimals, negative signs
                        if (newVal.all { !it.isDigit() }) {
                            return@OutlinedTextField
                        }

                        blockSetLockDurationDays = if (newVal == "") {
                            0
                        } else {
                            newVal.toInt()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = blockSetLockDurationDaysErrorMessage != null,
                    supportingText = {
                        Text(blockSetLockDurationDaysErrorMessage ?: "")
                    },
                    label = { Text("Days") }
                )
                OutlinedTextField(
                    blockSetLockDurationHours.toString(),
                    { newVal ->
                        if (newVal.all { !it.isDigit() }) {
                            return@OutlinedTextField
                        }

                        blockSetLockDurationHours = if (newVal == "") {
                            0
                        } else {
                            newVal.toInt()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = blockSetLockDurationHoursErrorMessage != null,
                    supportingText = {
                        Text(blockSetLockDurationHoursErrorMessage ?: "")
                    },
                    label = { Text("Hours") }
                )
                OutlinedTextField(
                    blockSetLockDurationMinutes.toString(),
                    { newVal ->
                        if (newVal.all { !it.isDigit() }) {
                            return@OutlinedTextField
                        }

                        blockSetLockDurationMinutes = if (newVal == "") {
                            0
                        } else {
                            newVal.toInt()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = blockSetLockDurationMinutesErrorMessage != null,
                    supportingText = {
                        Text(blockSetLockDurationMinutesErrorMessage ?: "")
                    },
                    label = { Text("Minutes") }
                )
            }

            Spacer(Modifier.height(8.dp))
            Text("Blocked app lock duration")
            Text("Amount of time a blocked app cannot be removed from a block list after being added. Meant to stop user from simply removing the app when an urge to use it hits")
            var blockListAppLockDurationInSeconds by remember { mutableIntStateOf(settings.appBlockListLockDurationAfterAddToBlockListInSeconds) }
            OutlinedTextField(
                blockListAppLockDurationInSeconds.toString(),
                {
                    blockListAppLockDurationInSeconds = it.toInt()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))
            Row {
                Button({
                    keyboardController?.hide()

                    val validationResult = viewModel.validateForm(
                        HoursMinutesDays(
                            days = blockSetLockDurationDays,
                            hours = blockSetLockDurationHours,
                            minutes = blockSetLockDurationMinutes
                        )
                    )

                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(message = "Settings updated!")
                    }
                }) {
                    Text("Save settings")
                }
                Spacer(Modifier.width(8.dp))
                Button({}) {
                    Text("Go back")
                }
            }

        }
    }
}
