package com.example.appblocker.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.APP_PACKAGE_BLOCK_LIST
import com.example.appblocker.AppBlockListPreferences
import com.example.appblocker.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


//TODO: decide immutability of appIcons map, decide data loading pattern in general for this composable. Current structure feels unsavvy
data class AddAppToBlockListScreenState(
    val apps: List<ApplicationInfo>,
    val blockedApps: List<AppBlockListPreferences>,
    val isLoading: Boolean
)

class AddAppToBlockListScreenViewModelFactory(private val dataStore: DataStore<List<AppBlockListPreferences>>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAppToBlockListScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddAppToBlockListScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddAppToBlockListScreenViewModel(val dataStore: DataStore<List<AppBlockListPreferences>>) :
    ViewModel() {
    private val _installedApps = MutableStateFlow<List<ApplicationInfo>>(listOf())
    private val _blockedApps = dataStore.data.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    val uiState = combine(_installedApps, _blockedApps) { installed, blocked ->
        AddAppToBlockListScreenState(
            apps = installed.filterNot { app ->
                blocked.any {
                    it.appPackageName == app.packageName
                }
            },
            blockedApps = blocked,
            isLoading = false,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddAppToBlockListScreenState(listOf(), listOf(), isLoading = true)
    )

    fun getAppList(pm: PackageManager) {
        _installedApps.update { _ ->
            val filteredApps =
                pm.getInstalledApplications(PackageManager.GET_META_DATA)
            filteredApps.filterNot { app ->
                val isCustomPackage = app.loadLabel(pm).startsWith("com.")
                isCustomPackage
            }
                .sortedBy { app: ApplicationInfo -> app.loadLabel(pm).toString() }
        }
    }

    fun addAppPackageToBlockList(appName: String, appPackageName: String) {
        viewModelScope.launch {
            dataStore.updateData { current ->
                current + AppBlockListPreferences(appName, appPackageName)
            }
        }
    }
}