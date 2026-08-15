package com.example.appblocker.ui.screens.blockSetDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppBlockItemPreferences
import com.example.appblocker.add
import com.example.appblocker.delete
import com.example.appblocker.lock_clock
import com.example.appblocker.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.collections.listOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Number of seconds after an app is added to the block list that user cannot remove it again.
 * Meant to be large so that user cannot after adding remove an app any time soon in a
 * moment of weakness
 */
const val LOCK_DURATION_AFTER_ADD_TO_BLOCK_LIST_IN_SECONDS = 60 * 60 * 24 * 7

@OptIn(ExperimentalTime::class)
@Composable
fun BlockSetDetailsScreen(
    viewModel: BlockSetDetailsScreenViewModel,
    id: Int,
    onNavigateToAddApp: (id: Int) -> Unit
) {
    val blockedAppSets by viewModel.blockSetFlow.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    val currBlockSet = blockedAppSets.find { bs -> bs.id == id }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (currBlockSet == null) {
        Text("Invalid id $id for block set")
    } else {
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
            floatingActionButton = {
                FloatingActionButton({ onNavigateToAddApp(id) }) {
                    Icon(add, "navigate to add app to block set blocklist screen")
                }
            }
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Block set: ${currBlockSet.name}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )

                if (currBlockSet.blockList.isEmpty()) {
                    Text(
                        "Block list empty. Tap + to add",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    currBlockSet.blockList.forEach { app ->
                        val now = Clock.System.now()
                        val isLocked =
                            (now.epochSeconds - app.addedAt.epochSeconds) < LOCK_DURATION_AFTER_ADD_TO_BLOCK_LIST_IN_SECONDS
                        BlockedAppItem(app, isLocked, {
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

@Composable
fun BlockedAppItem(
    blockedApp: AppBlockItemPreferences,
    isLocked: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
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
                IconButton(
                    {}, enabled = false,
                    modifier =
                        Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                ) {
                    Icon(
                        lock_clock,
                        "cannot remove from blocklist before waiting one day",
                    )
                }
            } else {
                Icon(
                    delete,
                    "remove from block list",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .clickable(onClick = onDelete)
                )
            }
        }
    }
}