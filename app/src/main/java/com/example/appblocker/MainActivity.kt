package com.example.appblocker

import android.app.ActivityManager
import android.view.accessibility.AccessibilityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.overscroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appblocker.ui.theme.AppBlockerTheme
import java.util.Calendar
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppBlockerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Log.d("MainActivity", "I started executing at least")
                    val context = LocalContext.current
                    checkAccessibilityPermission(context)
                    Greeting(
                        apps = listOf(),
                        modifier = Modifier.padding(innerPadding).padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AppOverlayer(packageName: String, modifier: Modifier ) {
    Button({

    }, modifier = modifier) {
        Text("Kill $packageName!")
    }
}

@Composable
fun Greeting(apps: List<String>, modifier: Modifier = Modifier) {
    Text("Number of apps: ${apps.size}")
    LazyColumn (modifier = Modifier.padding(10.dp)) {
        items(apps.size) {
            idx ->
            Text(
                text = "${idx + 1} ${apps[idx]}",
                modifier = modifier
            )
        }
    }
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppBlockerTheme {
        Greeting(listOf())
    }
}