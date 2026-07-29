package com.example.appblocker.ui.screens.blockSetDetails

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockSetPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BlockSetListScreenViewModelFactory(private val dataStore: DataStore<List<AppBlockSetPreferences>>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockSetListScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlockSetListScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BlockSetListScreenViewModel(val dataStore: DataStore<List<AppBlockSetPreferences>>) :
    ViewModel() {
    val blockSetsFLow: StateFlow<List<AppBlockSetPreferences>> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
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
