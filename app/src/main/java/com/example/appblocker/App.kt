package com.example.appblocker

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun App(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val hasAccessibilityPermission = hasAccessibilityPermission(context)
    if (!hasAccessibilityPermission) {
        checkAccessibilityPermission(context)
    }

    val hasSystemAlertPermission = Settings.canDrawOverlays(context)
    if (!hasSystemAlertPermission) {
        checkSystemAlertPermission(context)
    }

    if (hasAccessibilityPermission && hasSystemAlertPermission) {
        Text("App is working! You may close it")
    } else {
        Text("Missing required permissions to function, please grant accessibility and drawing over other apps permissions")
    }
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
