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
import androidx.compose.runtime.DisposableEffect
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.appblocker.ui.theme.GreenAction
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

import androidx.core.net.toUri

@Composable
fun App(
    modifier: Modifier = Modifier,
    onNavigateToManageBlockedApps: () -> Unit
) {
    var permissionCheckCounter by remember { mutableIntStateOf(0) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionCheckCounter++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val context = LocalContext.current

    val hasAccessibilityPermission =
        remember(permissionCheckCounter) { hasAccessibilityPermission(context) }
    val hasSystemAlertPermission =
        remember(permissionCheckCounter) { Settings.canDrawOverlays(context) }

    val hasNotificationPermission =
        remember(permissionCheckCounter) { hasNotificationPermission(context) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    var showPermissionDialog by remember { mutableStateOf(false) }
    if (showPermissionDialog) {
        PermissionDialog(
            showAccessibility = !hasAccessibilityPermission,
            showOverlay = !hasSystemAlertPermission,
            showNotification = !hasNotificationPermission,
            onDismiss = { showPermissionDialog = false },
            onGrantAccessibility = {
                requestAccessibilityPermission(context)
                showPermissionDialog = false
            },
            onGrantOverlay = {
                requestSystemAlertPermission(context)
                showPermissionDialog = false
            },
            {
                requestNotificationPermission(launcher)
                showPermissionDialog = false
            }
        )
    }

    Column() {
        if (hasAccessibilityPermission && hasSystemAlertPermission && hasNotificationPermission) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(16.dp)
            ) {
                Text(
                    "App is working! It will keep running the background, you may close it",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Button(onClick = onNavigateToManageBlockedApps) {
                    Text("Manage blocked apps")
                }
            }
        } else {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "App does not have required permissions to work. Click button below to grant them",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showPermissionDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenAction)
                ) {
                    Text("Grant permissions")
                }
            }
        }
    }
}

@Composable
fun PermissionDialog(
    showAccessibility: Boolean,
    showOverlay: Boolean,
    showNotification: Boolean,
    onDismiss: () -> Unit,
    onGrantAccessibility: () -> Unit,
    onGrantOverlay: () -> Unit,
    onGrantNotification: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permissions Required") },
        text = {
            Column {
                Text("This app needs permissions to function.")
                if (showAccessibility) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Accessibility: After clicking on the 'Grant Accessibility' button, click on 'Installed apps', select '${
                            stringResource(
                                R.string.app_name
                            )
                        }', and turn it ON."
                    )
                }
                if (showOverlay) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Overlay: Click the 'Grant Overlay' button, then click the toggle for ${
                            stringResource(
                                R.string.app_name
                            )
                        }."
                    )
                }
                if (showNotification) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Notification: It will only send ONE notification at start to show it's running. Needed so android doesn't stop the app from running in the background." +
                                "Click the 'Grant Notification' button, then click allow. "
                    )
                }
            }
        },
        confirmButton = {
            Column {
                if (showAccessibility) {
                    Button(
                        onClick = onGrantAccessibility,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenAction)
                    ) {
                        Text("Grant Accessibility")
                    }
                }
                if (showOverlay) {
                    Button(
                        onClick = onGrantOverlay,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenAction)
                    ) {
                        Text("Grant Overlay")
                    }
                }
                if (showNotification) {
                    Button(
                        onClick = onGrantNotification,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenAction)
                    ) {
                        Text("Grant Notification")
                    }
                }
            }
        },
    )
}

fun hasAccessibilityPermission(context: Context): Boolean {
    val accessibilityManager =
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    return accessibilityManager.getEnabledAccessibilityServiceList(
        android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    ).any { it.id.contains(context.packageName) }
}

fun hasNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // On Android 13+, must check for the POST_NOTIFICATIONS permission
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        // On Android 12 and below, notifications are granted at install time
        true
    }
}

fun requestAccessibilityPermission(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}

fun requestSystemAlertPermission(context: Context) {
    if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${context.packageName}".toUri()
        )
        context.startActivity(intent)
    }
}

fun requestNotificationPermission(launcher: ManagedActivityResultLauncher<String, Boolean>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}