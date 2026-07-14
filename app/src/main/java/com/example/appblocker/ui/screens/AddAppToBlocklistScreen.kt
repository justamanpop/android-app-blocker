package com.example.appblocker.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

//TODO: add a loading indicator for before page load.
@Composable
fun AddAppToBlocklistScreen(viewModel: AddAppToBlockListScreenViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val pm = context.packageManager
    LaunchedEffect(Unit) {
        viewModel.getAppList(pm)
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White,
                actionColor = Color.Yellow,
                snackbarData = data
            )
        }
    }) { padding ->
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.apps.size) { index ->
                    val appName =uiState.apps[index].appName
                    Row(Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            viewModel.addAppPackageToBlockList(
                                appName,
                                uiState.apps[index].appPackageName
                            )
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(message = "$appName added to block list!")
                            }
                        })) {
                        Text(
                            appName,
                            fontSize = 32.sp,
                            lineHeight = 32.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                }
            }
        }
    }

    Spacer(Modifier.height(10.dp))
}


