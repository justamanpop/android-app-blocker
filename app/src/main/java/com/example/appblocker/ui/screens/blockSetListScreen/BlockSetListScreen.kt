package com.example.appblocker.ui.screens.blockSetListScreen

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppBlockSetPreferences
import com.example.appblocker.add
import com.example.appblocker.delete
import com.example.appblocker.ui.shared.DaysOfWeekSelect
import com.example.appblocker.ui.shared.DeleteConfirmationModal
import com.example.appblocker.ui.theme.Background
import com.example.appblocker.ui.theme.Error
import com.example.appblocker.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun BlockSetListScreen(
    viewModel: BlockSetListScreenViewModel,
    navigateToAddBlockSet: () -> Unit,
    navigateToBlockSetDetails: (Int) -> Unit
) {
    val blockSets by viewModel.blockSetsFlow.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var blockSetToDelete by remember { mutableStateOf<AppBlockSetPreferences?>(null) }
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
            FloatingActionButton(navigateToAddBlockSet) {
                Icon(add, "navigate to add app to blocklist screen")
            }
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Block sets",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            if (blockSets.isEmpty()) {
                Text(
                    "No block sets exist. Tap + to create",
                    fontSize = 16.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            blockSets.forEach { blockSet ->
                key(blockSet.id) {
                    BlockSetListItem(
                        blockSet,
                        { navigateToBlockSetDetails(blockSet.id) },
                        { blockSetToDelete = blockSet },
                    )
                }
            }
        }
        val blockSetShadow = blockSetToDelete
        if (blockSetShadow != null) {
            DeleteConfirmationModal(blockSetShadow.name, {
                viewModel.deleteBlockSet(blockSetShadow.id)
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = "Block set ${blockSetShadow.name} deleted!")
                }
            }, {
                blockSetToDelete = null
                scope.launch {
                }
            })
        }
    }
}

@Composable
fun BlockSetListItem(
    blockSet: AppBlockSetPreferences,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                Column(
                    modifier = Modifier
                        .weight(9f)
                        .clickable(onClick = onClick)
                ) {
                    Text(
                        text = blockSet.name,
                        fontSize = 32.sp,
                        lineHeight = 32.sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${blockSet.blockList.size} apps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    if (blockSet.activeDays.values.all { !it }) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "⚠\uFE0F Block set not active on any day",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    } else {
                        Spacer(Modifier.height(8.dp))
                        DaysOfWeekSelect(
                            daysState = blockSet.activeDays,
                            readonly = true,
                            dayBoxSize = Pair(28.dp, 28.dp),
                            dayTextSize = 12.sp
                        )
                    }


                }
                Spacer(Modifier)
                Icon(
                    delete,
                    "delete block set",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .background(Error)
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown()
                                val job = scope.launch {
                                    progress.animateTo(1f, tween(1500))
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
