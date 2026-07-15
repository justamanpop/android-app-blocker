package com.example.appblocker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun BlockedAppListScreen(viewModel: BlockedAppListScreenViewModel) {
    val blockedAppList by viewModel.blockedAppPackageListFLow.collectAsStateWithLifecycle(
        initialValue = setOf()
    )
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
    ) { padding ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Blocked Apps List")
            blockedAppList.forEach { app ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = app.appName,
                        fontSize = 32.sp,
                        lineHeight = 32.sp,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        painterResource(android.R.drawable.ic_delete),
                        "remove from block list",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                            .background(Color.Red)
                            .clickable(onClick = {
                                viewModel.removePackageFromBlockList(
                                    app.appName,
                                    app.appPackageName
                                )
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(message = "${app.appName} removed from block list!")
                                }
                            })
                    )
                }
                Spacer(Modifier.height(4.dp))
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }

}
