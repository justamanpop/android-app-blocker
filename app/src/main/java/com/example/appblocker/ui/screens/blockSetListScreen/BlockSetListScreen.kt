package com.example.appblocker.ui.screens.blockSetListScreen

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.AppBlockSetPreferences
import com.example.appblocker.add
import com.example.appblocker.delete
import com.example.appblocker.ui.shared.DeleteConfirmationModal
import kotlinx.coroutines.launch

@Composable
fun BlockSetListScreen(
    viewModel: BlockSetListScreenViewModel,
    navigateToAddBlockSet: () -> Unit,
    navigateToBlockSetDetails: (Int) -> Unit
) {
    val blockSets by viewModel.blockSetsFLow.collectAsStateWithLifecycle(
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
        Column(modifier = Modifier
            .padding(scaffoldPadding)
            .padding(horizontal = 16.dp)) {
            Text("Block sets", fontWeight = FontWeight.SemiBold, fontSize = 24.sp, modifier = Modifier.padding(16.dp))
            blockSets.forEach { blockSet ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = blockSet.name,
                        fontSize = 32.sp,
                        lineHeight = 32.sp,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(9f)
                            .clickable(onClick = {
                                navigateToBlockSetDetails(blockSet.id)
                            })
                    )
                    Spacer(Modifier)
                    Icon(
                        delete,
                        "delete block set",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .clickable(onClick = {
                                blockSetToDelete = blockSet
                            })
                    )

                }
                Spacer(Modifier.height(4.dp))
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
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
            })
        }
    }
}
