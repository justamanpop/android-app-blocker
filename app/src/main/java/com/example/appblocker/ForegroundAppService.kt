package com.example.appblocker

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ForegroundAppService : AccessibilityService() {

    private var lastPackageName: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val packageName = event.packageName?.toString()
        if (packageName != null && packageName != lastPackageName) {
            lastPackageName = packageName
            if (packageName == "com.android.chrome") {
                Log.d("ForegroundAppService", "Chrome is in the foreground!")
            }
        }
    }

    override fun onInterrupt() {
        // Required, but usually left empty
    }
}
