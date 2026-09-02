package com.example.appblocker.ui.screens.settingsScreen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppSettingsPreferences
import com.example.appblocker.lock_clock
import com.example.appblocker.ui.shared.SecondaryButton
import com.example.appblocker.ui.shared.formatSeconds
import com.example.appblocker.ui.theme.TextPrimary
import com.example.appblocker.ui.theme.TextSecondary
import com.example.appblocker.ui.theme.Typography
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel, onGoBack: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
                initialValue = AppSettingsPreferences(0, 0, 0, Clock.System.now())
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

            Spacer(Modifier.height(20.dp))
            HorizontalDivider()

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

            Spacer(Modifier.height(20.dp))
            HorizontalDivider()

            Text("Settings lock duration", color = TextPrimary, style = Typography.titleMedium)
            Text(
                "Amount of time settings are locked after being edited. Meant to stop user from simply reducing other lock durations to zero when wanting to use a blocked app to get around lock durations",
                color = TextSecondary,
                style = Typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            var settingsLockDurationDays by remember { mutableStateOf("0") }
            var settingsLockDurationHours by remember { mutableStateOf("0") }
            var settingsLockDurationMinutes by remember { mutableStateOf("0") }

            LaunchedEffect(settings) {
                val fv = viewModel.getSettingsLockFieldValuesFromStoredSettings(settings)
                settingsLockDurationDays = fv.days
                settingsLockDurationHours = fv.hours
                settingsLockDurationMinutes = fv.minutes
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Days")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        settingsLockDurationDays,
                        { newVal ->
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }
                            settingsLockDurationDays = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Hours (0-23)")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        settingsLockDurationHours,
                        { newVal ->
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }

                            val newValInt = newVal.toIntOrNull()
                            if (newValInt != null && newValInt > 23) {
                                return@OutlinedTextField
                            }

                            settingsLockDurationHours = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Minutes (0-59)")
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        settingsLockDurationMinutes,
                        { newVal ->
                            if (newVal.any { !it.isDigit() }) {
                                return@OutlinedTextField
                            }

                            val newValInt = newVal.toIntOrNull()
                            if (newValInt != null && newValInt > 59) {
                                return@OutlinedTextField
                            }

                            settingsLockDurationMinutes = newVal
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Row {
                val secondsElapsed = now().epochSeconds - settings.lastUpdatedAt.epochSeconds
                val isUnlocked =
                    (secondsElapsed) >= settings.settingsLockDurationAfterEdit
                val lockDurationLeftInSeconds =
                    settings.settingsLockDurationAfterEdit - secondsElapsed
                Button(
                    {
                        keyboardController?.hide()
                        val blockDuration = viewModel.getDurationFromFieldValues(
                            blockSetLockDurationDays,
                            blockSetLockDurationHours,
                            blockSetLockDurationMinutes,
                            blockedAppLockDurationDays,
                            blockedAppLockDurationHours,
                            blockedAppLockDurationMinutes,
                            settingsLockDurationDays,
                            settingsLockDurationHours,
                            settingsLockDurationMinutes
                        )
                        viewModel.updateSettings(blockDuration)
                        focusManager.clearFocus()
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(message = "Settings updated!")
                        }
                    },
                    enabled = isUnlocked
                ) {
                    Text("Save settings")
                }
                Spacer(Modifier.width(8.dp))
                SecondaryButton(onClick = {
                    keyboardController?.hide()
                    onGoBack()
                }) {
                    Text("Go back")
                }
                if (isUnlocked) {
                    Spacer(Modifier.width(20.dp))
                    Button({
                        viewModel.relockSettings()
                    }) {
                        Text("Relock")
                    }
                } else {
                    Spacer(Modifier.width(24.dp))
                    val tooltipState = rememberTooltipState(isPersistent = true)
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Below
                        ),
                        tooltip = {
                            PlainTooltip() {
                                Text(
                                    "Settings can be updated in ${
                                        formatSeconds(
                                            lockDurationLeftInSeconds
                                        )
                                    }"
                                )
                            }
                        },
                        state = tooltipState,
                    ) {}
                    Icon(
                        lock_clock,
                        "cannot update settings before waiting for min lock duration",
                        modifier = Modifier
                            .clickable(onClick = { scope.launch { tooltipState.show() } })
                            .align(Alignment.CenterVertically)
                    )
                }
            }

        }
    }
}
