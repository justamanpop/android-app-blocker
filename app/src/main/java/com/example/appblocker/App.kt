package com.example.appblocker

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri


@Composable
fun App(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val hasAccessibilityPermission = hasAccessibilityPermission(context)
    val hasSystemAlertPermission = Settings.canDrawOverlays(context)

    var showPermissionDialog by remember { mutableStateOf(false) }
    if (showPermissionDialog) {
        PermissionDialog(
            showAccessibility = !hasAccessibilityPermission,
            showOverlay = !hasSystemAlertPermission,
            onDismiss = { showPermissionDialog = false },
            onGrantAccessibility = { checkAccessibilityPermission(context) },
            onGrantOverlay = { checkSystemAlertPermission(context) }
        )
    }

    if (hasAccessibilityPermission && hasSystemAlertPermission) {
        Text("App is working! You may close it", fontSize = 16.sp, modifier = modifier.padding(16.dp))
    } else {
        Column(modifier = modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("App does not have required permissions to work. Click button below to grant them", fontSize = 24.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button({showPermissionDialog = true})  {
                Text("Grant permissions")
            }
        }
    }
}

@Composable
fun PermissionDialog(
    showAccessibility: Boolean,
    showOverlay: Boolean,
    onDismiss: () -> Unit,
    onGrantAccessibility: () -> Unit,
    onGrantOverlay: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permissions Required") },
        text = {
            Column {
                Text("This app needs permissions to function.")
                if (showAccessibility) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Accessibility: After clicking on the 'Grant Accessibility' button, click on 'Installed apps', select 'AppBlocker', and turn it ON.")
                }
                if (showOverlay) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Overlay: Allow 'Display over other apps'.")
                }
            }
        },
        confirmButton = {
            Column {
                if (showAccessibility) {
                    Button(onClick = onGrantAccessibility, modifier = Modifier.fillMaxWidth()) {
                        Text("Grant Accessibility")
                    }
                }
                if (showOverlay) {
                    Button(onClick = onGrantOverlay, modifier = Modifier.fillMaxWidth()) {
                        Text("Grant Overlay")
                    }
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

fun hasAccessibilityPermission(context: Context): Boolean {
    val accessibilityManager =
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    return accessibilityManager.getEnabledAccessibilityServiceList(
        android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    ).any { it.id.contains(context.packageName) }
}

fun checkAccessibilityPermission(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}

fun checkSystemAlertPermission(context: Context) {
    if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${context.packageName}".toUri()
        )
        context.startActivity(intent)
    }
}
