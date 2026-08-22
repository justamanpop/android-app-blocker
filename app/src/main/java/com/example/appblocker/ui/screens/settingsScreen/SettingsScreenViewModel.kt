package com.example.appblocker.ui.screens.settingsScreen

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppSettingsPreferences
import kotlinx.coroutines.launch

class SettingsScreenViewModelFactory(
    private val dataStore: DataStore<AppSettingsPreferences>
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SettingsScreenViewModel(
    val dataStore: DataStore<AppSettingsPreferences>) : ViewModel() {

    fun updateSettings(blockListLockDurationAfterAppAdd: Int, blockSetLockDurationAfterBlockSetCreateOrUpdate: Int) {
        viewModelScope.launch {
            dataStore.updateData { preferences ->
                preferences.copy(appBlockListLockDurationAfterAddToBlockListInSeconds = blockListLockDurationAfterAppAdd, appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds = blockSetLockDurationAfterBlockSetCreateOrUpdate)
            }
        }
    }
}

