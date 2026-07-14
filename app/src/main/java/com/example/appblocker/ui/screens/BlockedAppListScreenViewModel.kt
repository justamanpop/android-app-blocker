package com.example.appblocker.ui.screens

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.APP_PACKAGE_BLOCK_LIST
import com.example.appblocker.AppBlockListPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class BlockedAppListScreenViewModelFactory(private val dataStore: DataStore<List<AppBlockListPreferences>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockedAppListScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlockedAppListScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BlockedAppListScreenViewModel(val dataStore: DataStore<List<AppBlockListPreferences>>) : ViewModel() {
    val blockedAppPackageListFLow: StateFlow<List<AppBlockListPreferences>> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )

    fun removePackageFromBlockList(appName: String, appPackageName: String) {
        viewModelScope.launch {
            dataStore.updateData { currentList ->
                currentList.filterNot { it.appName == appName && it.appPackageName == appPackageName }
            }
        }
    }
}
