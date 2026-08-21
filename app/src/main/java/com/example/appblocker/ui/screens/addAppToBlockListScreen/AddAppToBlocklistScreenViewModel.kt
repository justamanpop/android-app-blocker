package com.example.appblocker.ui.screens.addAppToBlockListScreen

import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockItemPreferences
import com.example.appblocker.AppBlockSetPreferences
import com.example.appblocker.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


data class AddAppToBlockListScreenState(
    val apps: List<AppNameInfo>,
    val blockSet: AppBlockSetPreferences?,
    val searchTerm: String = "",
    val filteredApps: List<AppNameInfo>,
)

data class AppNameInfo(val appName: String, val appPackageName: String)

class AddAppToBlockListScreenViewModelFactory(
    private val dataStore: DataStore<List<AppBlockSetPreferences>>, private val blockSetId: Int, private val appRepository: AppRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAppToBlockListScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddAppToBlockListScreenViewModel(dataStore, blockSetId, appRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddAppToBlockListScreenViewModel(
    val dataStore: DataStore<List<AppBlockSetPreferences>>, val blockSetId: Int, val appRepository: AppRepository,
) : ViewModel() {
    private val _blockedSets = dataStore.data.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    private val _searchTerm = MutableStateFlow("")

    val uiState =
        combine(appRepository.apps, _blockedSets, _searchTerm) { installed, blockSets, searchTerm ->
            val blockSet = blockSets.find { bs -> bs.id == blockSetId }
            val filteredApps = if (blockSet == null) {
                listOf()
            } else {
                installed.filterNot { app ->
                    blockSet.blockList.any {
                        it.appPackageName == app.appPackageName
                    }
                }.filter { app ->
                    app.appName.contains(searchTerm, ignoreCase = true)
                }
            }

            AddAppToBlockListScreenState(
                apps = installed,
                blockSet = blockSet,
                searchTerm = searchTerm,
                filteredApps = filteredApps
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AddAppToBlockListScreenState(listOf(), null, "", listOf())
        )

    @OptIn(ExperimentalTime::class)
    fun addAppPackageToBlockList(appName: String, appPackageName: String) {
        viewModelScope.launch {
            dataStore.updateData { preferences ->
                preferences.map { blockSet ->
                    if (blockSet.id == blockSetId) {
                        blockSet.copy(
                            blockList = blockSet.blockList + AppBlockItemPreferences(
                                appName,
                                appPackageName,
                                Clock.System.now()
                            ),
                            lastUpdatedAt = Clock.System.now()
                        )
                    } else {
                        blockSet
                    }
                }
            }
        }
    }

    fun updateSearchTerm(searchTerm: String) {
        _searchTerm.update { searchTerm }
    }
}