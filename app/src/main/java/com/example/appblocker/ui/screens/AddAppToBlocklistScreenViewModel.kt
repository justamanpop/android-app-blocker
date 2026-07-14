package com.example.appblocker.ui.screens

import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockListPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class AddAppToBlockListScreenState(
    val apps: List<AppNameInfo>,
    val blockedApps: List<AppBlockListPreferences>,
    val isLoading: Boolean
)

data class AppNameInfo(val appName: String, val appPackageName: String)

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
    private val _installedApps = MutableStateFlow<List<AppNameInfo>>(listOf())
    private val _blockedApps = dataStore.data.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    val uiState = combine(_installedApps, _blockedApps) { installed, blocked ->
        AddAppToBlockListScreenState(
            apps = installed.filterNot { app ->
                blocked.any {
                    it.appPackageName == app.appPackageName
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
        viewModelScope.launch {
            val allApps = withContext(Dispatchers.IO) {
                pm.getInstalledApplications(PackageManager.GET_META_DATA)
            }
            _installedApps.value = allApps.filterNot { app ->
                app.loadLabel(pm).startsWith("com.")
            }
                .map {app -> AppNameInfo(app.loadLabel(pm).toString(), app.packageName)}
                .sortedBy { app -> app.appName }
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