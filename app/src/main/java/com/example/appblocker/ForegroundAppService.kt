package com.example.appblocker

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi

class ForegroundAppService : AccessibilityService() {
    private var lastPackageName: String? = null
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate() {
        super.onCreate()
        
        // Setup Foreground Service
        val channelId = "app_blocker_service"
        val channel = NotificationChannel(
            channelId,
            "App Blocker Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("App Blocker is Active")
            .setContentText("Monitoring active apps to block them.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        startForeground(
            1,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        )

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val eventTime = event.eventTime // Time since boot in ms
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - eventTime > 10000) {
            return
        }

        val packageName = event.packageName?.toString()
        if (packageName != null && packageName != lastPackageName) {
            lastPackageName = packageName
            if (packageName == "com.android.chrome") {
                showOverlay()
            } else {
                if (packageName != getString(R.string.app_package_name)) {
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
        hideOverlay()
        super.onDestroy()
    }

    override fun onInterrupt() {
        // Required, but usually left empty
    }
}