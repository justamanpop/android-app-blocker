package com.example.appblocker.ui.screens.blockSetListScreen

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockSetPreferences
import com.example.appblocker.AppSettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class BlockSetListScreenViewModelFactory(private val dataStore: DataStore<List<AppBlockSetPreferences>>, private val settingsDataStore: DataStore<AppSettingsPreferences>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockSetListScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlockSetListScreenViewModel(dataStore, settingsDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@OptIn(ExperimentalTime::class)
class BlockSetListScreenViewModel(val dataStore: DataStore<List<AppBlockSetPreferences>>, val settingsDataStore: DataStore<AppSettingsPreferences>) :
    ViewModel() {
    val blockSetsFlow: StateFlow<List<AppBlockSetPreferences>> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )

    val settingsFlow: StateFlow<AppSettingsPreferences> =
        settingsDataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AppSettingsPreferences(0, 0,0, Clock.System.now())
            )

    fun deleteBlockSet(id: Int) {
        viewModelScope.launch {
            dataStore.updateData { curr ->
                val entryToDelete = curr.find { bs -> bs.id == id }
                if (entryToDelete == null) {
                    curr
                } else {
                    curr - entryToDelete
                }
            }
        }
    }
}
