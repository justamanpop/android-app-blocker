package com.example.appblocker.ui.screens.settingsScreen

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

    val settingsFlow: StateFlow<AppSettingsPreferences> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AppSettingsPreferences(0,0)
            )

    fun validateForm(blockSetLockDuration: HoursMinutesDays): SettingsFormValidationResult {
        val validationResult: SettingsFormValidationResult = SettingsFormValidationResult()
        if (blockSetLockDuration.days < 0) {
            validationResult.blockSetDayErrorMessage = "Days cannot be negative"
        }
        if (blockSetLockDuration.hours < 0) {
            validationResult.blockSetDayErrorMessage = "Hours cannot be negative"
        }
        if (blockSetLockDuration.minutes < 0) {
            validationResult.blockSetMinuteErrorMessage = "Minutes cannot be negative"
        }
        return validationResult
    }

    fun updateSettings(blockListLockDurationAfterAppAdd: Int, blockSetLockDurationAfterBlockSetCreateOrUpdate: Int) {
        viewModelScope.launch {
            dataStore.updateData { preferences ->
                preferences.copy(appBlockListLockDurationAfterAddToBlockListInSeconds = blockListLockDurationAfterAppAdd, appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds = blockSetLockDurationAfterBlockSetCreateOrUpdate)
            }
        }
    }
}

data class HoursMinutesDays(val hours: Int, val minutes: Int, val days: Int)
data class SettingsFormValidationResult(
    var blockSetDayErrorMessage: String? = null,
    var blockSetHourErrorMessage: String? = null,
    var blockSetMinuteErrorMessage: String? = null,
)
