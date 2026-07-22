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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class AddAppToBlockListScreenState(
    val apps: List<AppNameInfo>,
    val blockedApps: List<AppBlockListPreferences>,
    val searchTerm: String = "",
    val filteredApps: List<AppNameInfo>,
)

data class AppNameInfo(val appName: String, val appPackageName: String)

class AddAppToBlockListScreenViewModelFactory(
    private val dataStore: DataStore<List<AppBlockListPreferences>>
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAppToBlockListScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddAppToBlockListScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddAppToBlockListScreenViewModel(
    val dataStore: DataStore<List<AppBlockListPreferences>>
) :
    ViewModel() {
    private val _installedApps = MutableStateFlow<List<AppNameInfo>>(listOf())

    private val _blockedApps = dataStore.data.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    private val _searchTerm = MutableStateFlow("")

    val uiState = combine(_installedApps, _blockedApps, _searchTerm) { installed, blocked, searchTerm ->
        AddAppToBlockListScreenState(
            apps = installed,
            blockedApps = blocked,
            searchTerm = searchTerm,
            filteredApps = installed.filterNot { app ->
                blocked.any {
                    it.appPackageName == app.appPackageName
                }
            }.filter {
                app -> app.appName.contains(searchTerm, ignoreCase = true)
            }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddAppToBlockListScreenState(listOf(), listOf(), "", listOf())
    )

    fun getAppList(pm: PackageManager) {
        viewModelScope.launch {
            val processedApps = withContext(Dispatchers.IO) {
                val allApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                allApps
                    .map { app -> AppNameInfo(app.loadLabel(pm).toString(), app.packageName) }
                    .filterNot { app ->
                        app.appName.startsWith("com.")
                    }
                    .sortedBy { app -> app.appName }
            }
            _installedApps.value = processedApps
        }
    }

    fun addAppPackageToBlockList(appName: String, appPackageName: String) {
        viewModelScope.launch {
            dataStore.updateData { current ->
                current + AppBlockListPreferences(appName, appPackageName)
            }
        }
    }

    fun updateSearchTerm(searchTerm: String) {
        _searchTerm.update { searchTerm }
    }
}