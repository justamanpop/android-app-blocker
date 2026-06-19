package com.example.appblocker

import com.example.appblocker.R
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ForegroundAppService : AccessibilityService() {

    private var lastPackageName: String? = null
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val packageName = event.packageName?.toString()
        if (packageName != null && packageName != lastPackageName) {
            lastPackageName = packageName
            if (packageName == "com.android.chrome") {
                Log.d("ForegroundAppService", "showing overlay, package name is com.android.chrome")
                showOverlay()
            } else {
                Log.d("ForegroundAppService", "hiding overlay, package name is $packageName")
                //so that when overlay is shown the service doesn't instantly close it
                if (packageName != "com.example.appblocker") {
                    hideOverlay()
                }
            }
        }
    }

    private fun showOverlay() {
        if (overlayView == null) {
            val inflater = LayoutInflater.from(this)
            overlayView = inflater.inflate(R.layout.layout_overlay, null)

            val closeButton = overlayView?.findViewById<View>(R.id.btn_close)
            closeButton?.setOnClickListener {
                val homeIntent = android.content.Intent(android.content.Intent.ACTION_MAIN)
                homeIntent.addCategory(android.content.Intent.CATEGORY_HOME)
                homeIntent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(homeIntent)
                hideOverlay()
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            )
            params.gravity = Gravity.CENTER
            windowManager?.addView(overlayView, params)
        }
    }

    private fun hideOverlay() {
        if (overlayView != null) {
            windowManager?.removeView(overlayView)
            overlayView = null
        }
    }

    override fun onDestroy() {
        Log.d("ForegroundAppService", "in onDestroy hook")
        hideOverlay()
        super.onDestroy()
    }

    override fun onInterrupt() {
        // Required, but usually left empty
    }
}
