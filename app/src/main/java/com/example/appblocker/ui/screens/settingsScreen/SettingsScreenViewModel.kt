package com.example.appblocker.ui.screens.settingsScreen

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appblocker.AppSettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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

@OptIn(ExperimentalTime::class)
class SettingsScreenViewModel(
    val dataStore: DataStore<AppSettingsPreferences>
) : ViewModel() {

    val settingsFlow: StateFlow<AppSettingsPreferences> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AppSettingsPreferences(0, 0, 0,Clock.System.now() )
            )

    fun getBlockSetLockFieldValuesFromStoredSettings(storedSettings: AppSettingsPreferences): HoursMinutesDaysFieldValues {
        return HoursMinutesDaysFieldValues.fromSeconds(storedSettings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds)
    }

    fun getBlockListLockFieldValuesFromStoredSettings(storedSettings: AppSettingsPreferences): HoursMinutesDaysFieldValues {
        return HoursMinutesDaysFieldValues.fromSeconds(storedSettings.appBlockListLockDurationAfterAddToBlockListInSeconds)
    }

    fun getSettingsLockFieldValuesFromStoredSettings(storedSettings: AppSettingsPreferences): HoursMinutesDaysFieldValues {
        return HoursMinutesDaysFieldValues.fromSeconds(storedSettings.settingsLockDurationAfterEdit )
    }

    fun getDurationFromFieldValues(
        blockSetLockDaysFieldValue: String,
        blockSetLockHoursFieldValue: String,
        blockSetLockMinutesFieldValue: String,
        blockedAppLockDaysFieldValue: String,
        blockedAppLockHoursFieldValue: String,
        blockedAppLockMinutesFieldValue: String,
        settingsLockDaysFieldValue: String,
        settingsLockHoursFieldValue: String,
        settingsLockMinutesFieldValue: String,
    ): BlockDurations {
        return BlockDurations(
            blockedAppLockDurationAfterAppAddInSeconds = (blockedAppLockDaysFieldValue.toIntOrNull()
                ?: 0) * 3600 * 24 + (blockedAppLockHoursFieldValue.toIntOrNull()
                ?: 0) * 3600 + (blockedAppLockMinutesFieldValue.toIntOrNull() ?: 0) * 60,
            blockSetLockDurationAfterBlockSetCreateOrUpdateInSeconds = (blockSetLockDaysFieldValue.toIntOrNull()
                ?: 0) * 3600 * 24 + (blockSetLockHoursFieldValue.toIntOrNull()
                ?: 0) * 3600 + (blockSetLockMinutesFieldValue.toIntOrNull() ?: 0) * 60,
            settingsLockDurationAfterEditInSeconds = (settingsLockDaysFieldValue.toIntOrNull()
                ?: 0) * 3600 * 24 + (settingsLockHoursFieldValue.toIntOrNull()
                ?: 0) * 3600 + (settingsLockMinutesFieldValue.toIntOrNull() ?: 0) * 60
        )
    }

    fun updateSettings(
        blockDurations: BlockDurations
    ) {
        viewModelScope.launch {
            dataStore.updateData { preferences ->
                preferences.copy(
                    appBlockListLockDurationAfterAddToBlockListInSeconds = blockDurations.blockedAppLockDurationAfterAppAddInSeconds,
                    appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds = blockDurations.blockSetLockDurationAfterBlockSetCreateOrUpdateInSeconds,
                    settingsLockDurationAfterEdit = blockDurations.settingsLockDurationAfterEditInSeconds,
                    lastUpdatedAt = Clock.System.now()
                )
            }
        }
    }
}

data class HoursMinutesDaysFieldValues(val minutes: String, val hours: String, val days: String) {
    companion object {
        fun fromSeconds(seconds: Int): HoursMinutesDaysFieldValues {
            val days = (seconds / 3600) / 24
            val hours =
                (seconds- days * 24 * 3600) / 3600
            val minutes =
                ((seconds - days * 24 * 3600) - (hours * 3600))/60
            return HoursMinutesDaysFieldValues(
                days = days.toString(),
                hours = hours.toString(),
                minutes = minutes.toString()
            )
        }
    }
}

data class BlockDurations(
    val blockedAppLockDurationAfterAppAddInSeconds: Int,
    val blockSetLockDurationAfterBlockSetCreateOrUpdateInSeconds: Int,
    val settingsLockDurationAfterEditInSeconds: Int
)