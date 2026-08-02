@file:OptIn(ExperimentalTime::class)

package com.example.appblocker.ui.screens.blockedAppListScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.add
import com.example.appblocker.delete
import com.example.appblocker.lock_clock
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Number of seconds after an app is added to the block list that user cannot remove it again.
 */
const val LOCK_DURATION_AFTER_ADD_TO_BLOCK_LIST_IN_SECONDS = 60*60*24*2
@Composable
//fun BlockedAppListScreen(viewModel: BlockedAppListScreenViewModel, onNavigateToAddApp: (id: Int) -> Unit) {
fun BlockedAppListScreen(id: Int) {
    Text("Placeholder page of block list of block set with id $id")
}
