package com.example.appblocker

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun App(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    checkAccessibilityPermission(context)
    checkSystemAlertPermission(context)
}

fun checkAccessibilityPermission(context: Context) {
    val accessibilityManager =
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val isEnabled = accessibilityManager.getEnabledAccessibilityServiceList(
        android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    ).any { it.resolveInfo.serviceInfo.packageName == context.packageName }

    if (!isEnabled) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }
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
