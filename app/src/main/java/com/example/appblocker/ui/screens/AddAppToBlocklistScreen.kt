package com.example.appblocker.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddAppToBlocklistScreen(viewModel: AddAppToBlockListScreenViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val pm = context.packageManager
    LaunchedEffect(Unit) {
        viewModel.getAppList(pm)
    }

    if (uiState.isLoading) {
        Text("Loading...")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.apps.size) { index ->
                Row() {
                    Text(
                        "${uiState.apps[index].loadLabel(pm)}",
                        fontSize = 32.sp,
                        lineHeight = 32.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    /*Icon(
                        viewModel.getAppIcon(uiState.apps, labels, pm, index),
                        contentDescription = "icon of ${uiState.apps[index].loadLabel(pm)})"
                    )*/
                }
                Spacer(Modifier.height(4.dp))
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }

}


