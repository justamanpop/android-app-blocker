package com.example.appblocker.ui.screens.addBlockSetScreen

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockSetPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AddBlockSetScreenViewModelFactory(private val dataStore: DataStore<List<AppBlockSetPreferences>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBlockSetScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddBlockSetScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddBlockSetScreenViewModel(val dataStore: DataStore<List<AppBlockSetPreferences>>) : ViewModel() {
}
