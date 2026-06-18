package com.example.appblocker

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ForegroundAppService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Only listen for window state changes (when an app comes to foreground)
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName != null) {
                Log.d("ForegroundAppService", "Foreground app: $packageName")
            }
        }
    }

    override fun onInterrupt() {
        // Required, but usually left empty
    }
}
