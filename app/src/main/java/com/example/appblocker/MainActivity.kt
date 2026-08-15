package com.example.appblocker

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.appblocker.ui.screens.addAppToBlockListScreen.AppNameInfo
import com.example.appblocker.ui.theme.AppBlockerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    lateinit var appRepository: AppRepository
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appRepository = AppRepository(packageManager)
        CoroutineScope(Dispatchers.IO).launch {
            appRepository.loadApps()
        }

        enableEdgeToEdge()
        setContent {
            AppBlockerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding), appRepository)
                }
            }
        }
    }
}

class AppRepository(private val packageManager: PackageManager) {
    private val _apps = MutableStateFlow<List<AppNameInfo>>(emptyList())
    val apps = _apps.asStateFlow()

    fun loadApps() {
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        _apps.value = apps.map { app ->
            AppNameInfo(
                app.loadLabel(packageManager).toString(),
                app.packageName
            )
        }
            .filterNot { app ->
                app.appName.startsWith("com.")
            }
            .sortedBy { app -> app.appName }
    }
}
