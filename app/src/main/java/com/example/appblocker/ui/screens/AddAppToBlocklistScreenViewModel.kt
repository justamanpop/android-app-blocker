package com.example.appblocker.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//TODO: decide immutability of appIcons map, decide data loading pattern in general for this composable. Current structure feels unsavvy
data class AddAppToBlockListScreenState(
    val apps: List<ApplicationInfo>,
    val appIcons: Map<CharSequence, ImageBitmap>,
    val isLoading: Boolean
)

class AddAppToBlockListScreenViewModel : ViewModel() {
    private val _uiState =
        MutableStateFlow(AddAppToBlockListScreenState(listOf(), mutableMapOf(), isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun getAppList(pm: PackageManager) {
        _uiState.update { state ->
            val filteredApps = pm.getInstalledApplications(PackageManager.GET_META_DATA).filterNot { app ->
//                    app.loadLabel(pm).startsWith("com.")
                val isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                val isUpdatedSystemApp =
                    (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                isSystemApp || isUpdatedSystemApp
            }
                .sortedBy { app: ApplicationInfo -> app.loadLabel(pm).toString()}
            state.copy(
                apps = filteredApps,
                isLoading = false
            )
        }
    }

    fun getAppIcon(
        apps: List<ApplicationInfo>,
        labels: List<CharSequence>,
        pm: PackageManager,
        index: Int
    ): ImageBitmap {
        val appName = labels[index]
        val valInMap = _uiState.value.appIcons[appName]
        if (valInMap == null) {
            val bitmap = apps[index].loadIcon(pm).toBitmap().asImageBitmap()
            _uiState.update { state ->
                state.copy(
                    appIcons = state.appIcons + (labels[index] to bitmap)
                )
            }
            return bitmap
        }
        return valInMap
    }
}