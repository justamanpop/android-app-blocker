package com.example.appblocker.ui.screens.addBlockSetScreen

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppBlockSetPreferences
import com.example.appblocker.CreateBlockSetFormValidationResult
import com.example.appblocker.validateActiveTime
import com.example.appblocker.validateBlockSetName
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

    fun validateForm(
        blockSetName: String,
        activeTime: String,
        blockSets: List<AppBlockSetPreferences>
    ): CreateBlockSetFormValidationResult {
        val nameErrorMessage: String? = validateBlockSetName(blockSetName, blockSets, false)
        val activeTimeErrorMessage = validateActiveTime(activeTime)

        return CreateBlockSetFormValidationResult(nameErrorMessage, activeTimeErrorMessage)
    }

    fun createBlockSet(
        blockSetName: String,
        activeDays: Map<DayOfWeek, Boolean>,
        activeTime: String
    ) {
        val orderedActiveTime = activeTime.split(",").sorted().joinToString(",")
        viewModelScope.launch {
            dataStore.updateData { curr ->
                val maxId = curr.maxByOrNull { it.id }?.id ?: 1
                curr + AppBlockSetPreferences(
                    maxId + 1,
                    blockSetName,
                    listOf(),
                    activeDays,
                    orderedActiveTime
                )
            }
        }
    }
}