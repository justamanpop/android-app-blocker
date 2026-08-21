package com.example.appblocker.ui.screens.blockSetDetails

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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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
    val dataStore: DataStore<List<AppBlockSetPreferences>>,
    val blockSetId: Int
) :
    ViewModel() {
    val blockSetFlow: StateFlow<List<AppBlockSetPreferences>> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )

    @OptIn(ExperimentalTime::class)
    fun removePackageFromBlockList(appPackageName: String) {
        viewModelScope.launch {
            dataStore.updateData { preferences ->
                preferences.map {
                    blockSet ->
                    if (blockSet.id == blockSetId) {
                        blockSet.copy(blockList = blockSet.blockList.filterNot { app ->  app.appPackageName == appPackageName})
                    } else {
                       blockSet
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun updateBlockSet(name: String, activeDays: Map<DayOfWeek, Boolean>, activeTime: String) {
        viewModelScope.launch {
            dataStore.updateData { preferences ->
                preferences.map {
                        blockSet ->
                    if (blockSet.id == blockSetId) {
                        blockSet.copy(name = name, activeDays = activeDays, activeTime = activeTime, lastUpdatedAt = Clock.System.now())
                    } else {
                        blockSet
                    }
                }
            }
        }
    }

    fun validateForm(
        blockSetName: String,
        activeTime: String,
        blockSets: List<AppBlockSetPreferences>
    ): CreateBlockSetFormValidationResult {
        val nameErrorMessage: String? = validateBlockSetName(blockSetName, blockSets, true)
        val activeTimeErrorMessage = validateActiveTime(activeTime)

        return CreateBlockSetFormValidationResult(nameErrorMessage, activeTimeErrorMessage)
    }
}
