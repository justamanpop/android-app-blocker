package com.example.appblocker.ui.screens.blockSetDetails

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockSetPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BlockSetDetailsScreenViewModelFactory(
    private val dataStore: DataStore<List<AppBlockSetPreferences>>,
    private val blockSetId: Int
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockSetDetailsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlockSetDetailsScreenViewModel(dataStore, blockSetId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BlockSetDetailsScreenViewModel(
    dataStore: DataStore<List<AppBlockSetPreferences>>,
    blockSetId: Int
) :
    ViewModel() {
    private val blockSetsFLow: StateFlow<List<AppBlockSetPreferences>> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )
    val blockSet = blockSetsFLow.map { list ->
        list.find { it.id == blockSetId }
    }
}
