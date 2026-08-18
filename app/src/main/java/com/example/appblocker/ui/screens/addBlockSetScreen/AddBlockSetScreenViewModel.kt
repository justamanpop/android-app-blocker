package com.example.appblocker.ui.screens.addBlockSetScreen

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockSetPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

class AddBlockSetScreenViewModelFactory(private val dataStore: DataStore<List<AppBlockSetPreferences>>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBlockSetScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddBlockSetScreenViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddBlockSetScreenViewModel(val dataStore: DataStore<List<AppBlockSetPreferences>>) :
    ViewModel() {
    val blockSetsFlow: StateFlow<List<AppBlockSetPreferences>> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )

    fun validateForm(blockSetName: String, activeTime: String, blockSets: List<AppBlockSetPreferences>): CreateBlockSetFormValidationResult {
        var nameErrorMessage: String? = null
        var activeTimeErrorMessage: String? = null

        if (blockSetName == "") {
            nameErrorMessage =  "Block set name cannot be empty"
        }
        if (blockSets.any { bs -> bs.name == blockSetName }) {
            nameErrorMessage = "Block set with name $blockSetName already exists"
        }

        if (activeTime == "") {
            activeTimeErrorMessage =  "Block time cannot be empty. Click All Day to enable it at all times"
        }
        return CreateBlockSetFormValidationResult(nameErrorMessage, activeTimeErrorMessage)
    }

    fun createBlockSet(blockSetName: String, activeDays: Map<DayOfWeek, Boolean>, activeTime: String) {
        viewModelScope.launch {
            dataStore.updateData { curr ->
                val maxId = curr.maxByOrNull { it.id }?.id ?: 1
                curr + AppBlockSetPreferences(maxId + 1, blockSetName, listOf(), activeDays, activeTime)
            }
        }
    }
}

data class CreateBlockSetFormValidationResult(val nameErrorMessage: String?, val activeTimeErrorMessage: String?)
