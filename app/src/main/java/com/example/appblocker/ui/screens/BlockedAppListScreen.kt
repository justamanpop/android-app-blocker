package com.example.appblocker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BlockedAppListScreen(viewModel: BlockedAppListScreenViewModel) {
    val blockedAppList by viewModel.blockedAppPackageListFLow.collectAsStateWithLifecycle(initialValue = setOf())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Blocked Apps List")
        blockedAppList.forEach { app ->
            Text(text = app.appName, Modifier.clickable(onClick = {
                viewModel.removePackageFromBlockList(app.appName, app.appPackageName)
            }))
        }
    }
}
