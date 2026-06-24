package com.example.appblocker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appblocker.getAppList

@Composable
fun AddAppToBlocklistScreen() {
    val context = LocalContext.current
    val apps = getAppList(context)

    LazyColumn(Modifier.padding(8.dp)) {
        items(apps.size) { index ->
            Row() {
                Text("${apps[index].loadLabel(context.packageManager)}", fontSize = 32.sp, modifier = Modifier.padding(start = 16.dp))
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }
}
