package com.example.appblocker.ui.screens.addBlockSetScreen

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

    fun validateForm(
        blockSetName: String,
        activeTime: String,
        blockSets: List<AppBlockSetPreferences>
    ): CreateBlockSetFormValidationResult {
        val nameErrorMessage: String? = validateBlockSetName(blockSetName, blockSets)
        val activeTimeErrorMessage = validateActiveTime(activeTime)

        return CreateBlockSetFormValidationResult(nameErrorMessage, activeTimeErrorMessage)
    }

    private fun validateBlockSetName(
        blockSetName: String,
        blockSets: List<AppBlockSetPreferences>
    ): String? {
        if (blockSetName == "") {
            return "Block set name cannot be empty"
        }
        if (blockSets.any { bs -> bs.name == blockSetName }) {
            return "Block set with name $blockSetName already exists"
        }
        return null
    }

    private fun validateActiveTime(
        activeTime: String,
    ): String? {
        if (activeTime == "") {
            return "Block time cannot be empty. Click All Day to enable it at all times"
        }
        
        val timeRanges = activeTime.split(",")
        val invalidTimeRangeErrorMessage = { timeRange: String -> "Time range $timeRange is invalid"}

        for (timeRange in timeRanges) {
            if (timeRange.length != 9) {
                return invalidTimeRangeErrorMessage(timeRange)
            }

            val splitValues = timeRange.split("-")
            val startTime = splitValues[0]
            val endTime = splitValues[1]
            if (splitValues.size != 2 || startTime >= endTime || startTime.length != 4 || endTime.length !=4) {
                return invalidTimeRangeErrorMessage(timeRange)
            }

            val startHour = startTime.substring(0,2)
            val startMinute = startTime.substring(2,4)
            if(startHour.toIntOrNull() == null || startMinute.toIntOrNull() == null) {
                return invalidTimeRangeErrorMessage(timeRange)
            }

            if (startHour.toInt() !in 0..24 || startMinute.toInt() !in 0..59) {
                return invalidTimeRangeErrorMessage(timeRange)
            }
        }
        return null
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

data class CreateBlockSetFormValidationResult(
    val nameErrorMessage: String?,
    val activeTimeErrorMessage: String?
)
