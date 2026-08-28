package com.example.appblocker.ui.screens.blockSetDetails

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppBlockItemPreferences
import com.example.appblocker.AppSettingsPreferences
import com.example.appblocker.delete
import com.example.appblocker.info_i
import com.example.appblocker.lock_clock
import com.example.appblocker.ui.shared.SecondaryButton
import com.example.appblocker.ui.shared.DaysOfWeekSelect
import com.example.appblocker.ui.shared.formatSeconds
import com.example.appblocker.ui.theme.Border
import com.example.appblocker.ui.theme.Error
import com.example.appblocker.ui.theme.OnPrimary
import com.example.appblocker.ui.theme.Primary
import com.example.appblocker.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.collections.listOf
import kotlin.time.Clock
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun BlockSetDetailsScreen(
    viewModel: BlockSetDetailsScreenViewModel,
    id: Int,
    onNavigateToAddApp: (id: Int) -> Unit,
    onGoBack: () -> Unit,
) {
    val blockSets by viewModel.blockSetFlow.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    val currBlockSet = blockSets.find { bs -> bs.id == id }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (currBlockSet == null) {
        Text("Invalid id $id for block set")
    } else {
        val keyboardController = LocalSoftwareKeyboardController.current

        var nameTextFieldValue by remember { mutableStateOf(currBlockSet.name) }
        var nameTextFieldError by rememberSaveable { mutableStateOf<String?>(null) }

        var activeDays by remember { mutableStateOf(currBlockSet.activeDays) }

        var activeTimeTextFieldValue by remember { mutableStateOf(currBlockSet.activeTime) }
        var activeTimeTextFieldError by rememberSaveable { mutableStateOf<String?>(null) }

        val localFocusManager = LocalFocusManager.current

        val settings by viewModel.settingsFlow.collectAsStateWithLifecycle(
            initialValue = AppSettingsPreferences(0,0,0, Clock.System.now())
        )

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
            },
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Block Set ${currBlockSet.name}",
                    fontSize = 32.sp,
                    lineHeight = 32.sp,
                )
                Spacer(Modifier.height(8.dp))
                val isLocked =
                    (now().epochSeconds - currBlockSet.lastUpdatedAt.epochSeconds) < settings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds
                OutlinedTextField(
                    nameTextFieldValue,
                    {
                        nameTextFieldValue = it
                    },
                    label = { Text("Name") },
                    placeholder = { Text("Block Set 1") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    singleLine = true,
                    isError = nameTextFieldError != null,
                    supportingText = {
                        Text(nameTextFieldError ?: "")
                    },
                    readOnly = isLocked
                )

                Spacer(Modifier.height(8.dp))

                Text("Days of week", fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                DaysOfWeekSelect(
                    daysState = activeDays,
                    readonly = isLocked,
                    modifier = Modifier
                        .border(1.dp, Border, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    onDayClick = { days ->
                        activeDays = days
                        localFocusManager.clearFocus()
                    },
                )

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Time", fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    val tooltipState = rememberTooltipState(isPersistent = true)
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Below
                        ),
                        tooltip = { PlainTooltip() { Text("Enter time ranges in 24 hour format separated by commas\nE.g 0000-1300,1500-2300.\nOrder does not matter") } },
                        state = tooltipState,
                    ) {
                        Icon(
                            info_i,
                            "time form field info",
                            modifier = Modifier
                                .background(Color.Gray, CircleShape)
                                .size(16.dp)
                                .clickable(onClick = { scope.launch { tooltipState.show() } })
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    activeTimeTextFieldValue,
                    {
                        activeTimeTextFieldValue = it
                    },
                    placeholder = { Text("0000-1300,1500-2000") },
                    singleLine = true,
                    isError = activeTimeTextFieldError != null,
                    supportingText = {
                        Text(activeTimeTextFieldError ?: "")
                    },
                    readOnly = isLocked
                )
                Spacer(Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        enabled = !isLocked,
                        onClick = {
                            keyboardController?.hide()

                            val validationResult = viewModel.validateForm(
                                nameTextFieldValue,
                                activeTimeTextFieldValue,
                                blockSets
                            )
                            if (validationResult.nameErrorMessage != null) {
                                nameTextFieldError = validationResult.nameErrorMessage
                                return@Button
                            }
                            if (validationResult.activeTimeErrorMessage != null) {
                                activeTimeTextFieldError = validationResult.activeTimeErrorMessage
                                return@Button
                            }

                            viewModel.updateBlockSet(
                                nameTextFieldValue,
                                activeDays,
                                activeTimeTextFieldValue
                            )

                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(message = "Block set updated!")
                            }
                        }) {
                        Text("Update")
                    }
                    Spacer(Modifier.width(8.dp))
                    SecondaryButton(onClick = onGoBack) {
                        Text("Go back")
                    }
                    if (isLocked) {
                        Spacer(Modifier.width(24.dp))
                        val tooltipState = rememberTooltipState(isPersistent = true)
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                                TooltipAnchorPosition.Below
                            ),
                            tooltip = { PlainTooltip() { Text("Block set cannot be edited for ${formatSeconds(settings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds)} seconds after creation or updating") } },
                            state = tooltipState,
                        ) {}
                        Icon(
                            lock_clock,
                            "cannot remove from blocklist before waiting for min duration",
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.CenterVertically)
                                .clickable(onClick = { scope.launch { tooltipState.show() } })
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Block list", fontSize = 32.sp)
                    Spacer(Modifier.width(20.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(32.dp))
                            .clickable(onClick = {
                                onNavigateToAddApp(
                                    currBlockSet.id
                                )
                            })
                            .align(Alignment.CenterVertically)
                            .background(Primary)
                            .padding(4.dp)
                    ) {
                        Text("+ Add app to block list", fontSize = 12.sp, color = OnPrimary)
                    }
                }
                if (currBlockSet.blockList.isEmpty()) {
                    Text(
                        "Block list empty.",
                        fontSize = 24.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    Spacer(Modifier.padding(top = 16.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 4.dp)
                    ) {
                        items(currBlockSet.blockList.size) { idx ->
                            val app = currBlockSet.blockList[idx]
                            val isLocked =
                                (now().epochSeconds - app.addedAt.epochSeconds) < settings.appBlockListLockDurationAfterAddToBlockListInSeconds
                            key(app.appPackageName) {
                                BlockedAppItem(app, isLocked, settings.appBlockListLockDurationAfterAddToBlockListInSeconds, {
                                    viewModel.removePackageFromBlockList(app.appPackageName)
                                    scope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        snackbarHostState.showSnackbar(message = "${app.appName} removed from block list!")
                                    }
                                })
                            }
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAppItem(
    blockedApp: AppBlockItemPreferences,
    isLocked: Boolean,
    blockDurationInSeconds: Int,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding( bottom = 8.dp, end = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
        ) {
            Box(
                Modifier
                    .matchParentSize()
                    .graphicsLayer() {
                        scaleY = progress.value
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    }
                    .background(Error.copy(0.6f))
            ) {}
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = blockedApp.appName,
                    fontSize = 32.sp,
                    lineHeight = 32.sp,
                    color = if (isLocked) Color.Gray else Color.White,
                    modifier = Modifier.weight(9f)
                )
                Spacer(Modifier)
                if (isLocked) {
                    val tooltipState = rememberTooltipState(isPersistent = true)
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Below
                        ),
                        tooltip = { PlainTooltip() { Text("Blocked app cannot be removed from list for ${formatSeconds(blockDurationInSeconds)} after being added") } },
                        state = tooltipState,
                    ) {}
                    Icon(
                        lock_clock,
                        "cannot remove from blocklist before waiting one day",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .clickable(onClick = { scope.launch { tooltipState.show() } })
                    )
                } else {
                    Icon(
                        delete,
                        "remove from block list",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .pointerInput(Unit) {
                                awaitEachGesture {
                                    awaitFirstDown()
                                    val job = scope.launch {
                                        progress.animateTo(1f, tween(1000))
                                        onDelete()
                                        progress.animateTo(0f, tween(200))
                                    }
                                    waitForUpOrCancellation()
                                    job.cancel()
                                    if (progress.value < 1f) {
                                        scope.launch {
                                            progress.animateTo(0f, tween(200))
                                        }
                                    }
                                }
                            }
                    )
                }
            }
        }
    }
}