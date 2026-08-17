package com.example.appblocker.ui.screens.addBlockSetScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.ui.theme.Border
import com.example.appblocker.ui.theme.Surface
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

@Composable
fun AddBlockSetScreen(viewModel: AddBlockSetScreenViewModel, onAddBlockSet: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var nameTextFieldValue by remember { mutableStateOf("") }

    var daysActive by remember {
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

    var nameTextFieldError by rememberSaveable { mutableStateOf<String?>(null) }

    val focusRequester = remember { FocusRequester() }
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
            Spacer(Modifier.height(2.dp))
            OutlinedTextField(
                nameTextFieldValue, {
                    nameTextFieldValue = it
                },
                label = { Text("Name") },
                placeholder = { Text("Block Set 1") },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                isError = nameTextFieldError != null,
                supportingText = {
                    val error = nameTextFieldError
                    if (error != null) {
                        Text(error)
                    }
                },
                modifier = Modifier.focusRequester(focusRequester)
            )

            Spacer(Modifier.height(8.dp))
            DaysOfWeekFormField(daysActive, { d -> daysActive = d })

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val blockSetToCreateName = nameTextFieldValue
                    keyboardController?.hide()

                    val validationResult = viewModel.validateBlockSetName(blockSetToCreateName)
                    if (validationResult.isFailure) {
                        nameTextFieldError = validationResult.exceptionOrNull()?.message
                        return@Button
                    }

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

@Composable
fun DaysOfWeekFormField(
    daysState: Map<DayOfWeek, Boolean>,
    updateDaysState: (daysState: Map<DayOfWeek, Boolean>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Days of week", fontSize = 16.sp)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Border, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            DayOfWeek.entries.forEach { d ->
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(Surface)
                        .height(40.dp)
                        .width(40.dp)
                        .padding(4.dp)
                        .clickable(onClick = { updateDaysState(daysState + (d to !daysState[d]!!)) })
                ) {
                    Text(
                        d.toString().substring(0, 1),
                        color = if (daysState[d] == true) Color.Green else Color.Red,
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}