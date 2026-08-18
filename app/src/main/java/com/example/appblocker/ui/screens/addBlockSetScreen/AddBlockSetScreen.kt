package com.example.appblocker.ui.screens.addBlockSetScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.info_i
import com.example.appblocker.ui.shared.DaysOfWeekSelect
import com.example.appblocker.ui.theme.Border
import com.example.appblocker.ui.theme.OnPrimary
import com.example.appblocker.ui.theme.Primary
import com.example.appblocker.ui.theme.Surface
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBlockSetScreen(viewModel: AddBlockSetScreenViewModel, onAddBlockSet: () -> Unit) {
    val blockSets by viewModel.blockSetsFlow.collectAsStateWithLifecycle(
        initialValue = listOf()
    )

    val keyboardController = LocalSoftwareKeyboardController.current

    var nameTextFieldValue by remember { mutableStateOf("") }
    var nameTextFieldError by rememberSaveable { mutableStateOf<String?>(null) }

    var activeDays by remember {
        mutableStateOf(
            mapOf(
                DayOfWeek.SUNDAY to false,
                DayOfWeek.MONDAY to false,
                DayOfWeek.TUESDAY to false,
                DayOfWeek.WEDNESDAY to false,
                DayOfWeek.THURSDAY to false,
                DayOfWeek.FRIDAY to false,
                DayOfWeek.SATURDAY to false,
            )
        )
    }

    var activeTimeTextFieldValue by remember { mutableStateOf("") }
    var activeTimeTextFieldError by rememberSaveable { mutableStateOf<String?>(null) }

    val focusRequester = remember { FocusRequester() }
    val localFocusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "Create block set",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            OutlinedTextField(
                nameTextFieldValue, {
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
                modifier = Modifier.focusRequester(focusRequester)
            )

            Spacer(Modifier.height(8.dp))

            Text("Days of week", fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            DaysOfWeekSelect(
                daysState = activeDays,
                readonly = false,
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
            Row {

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
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .height(44.dp)
                        .width(36.dp)
                        .background(Primary)
                        .padding(4.dp)
                        .clickable(onClick = {
                            activeTimeTextFieldValue = "0000-2400"
                        })
                ) {
                    Text("All Day", fontSize = 12.sp, lineHeight = 16.sp, color = OnPrimary)
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    keyboardController?.hide()

                    val validationResult = viewModel.validateForm(nameTextFieldValue, activeTimeTextFieldValue, blockSets)
                    if (validationResult.nameErrorMessage != null) {
                        nameTextFieldError = validationResult.nameErrorMessage
                        return@Button
                    }
                    if (validationResult.activeTimeErrorMessage != null) {
                        activeTimeTextFieldError = validationResult.activeTimeErrorMessage
                        return@Button
                    }

                    viewModel.createBlockSet(nameTextFieldValue, activeDays, activeTimeTextFieldValue)

                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(message = "Block set $nameTextFieldValue created!")
                    }
                    onAddBlockSet()
                }) {
                Text("Create block set")
            }
        }
    }
}

