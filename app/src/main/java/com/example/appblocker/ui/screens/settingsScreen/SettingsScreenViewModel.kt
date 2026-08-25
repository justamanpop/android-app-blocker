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
    val dataStore: DataStore<AppSettingsPreferences>
) : ViewModel() {

    val settingsFlow: StateFlow<AppSettingsPreferences> =
        dataStore.data
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AppSettingsPreferences(0, 0)
            )

    fun getBlockSetLockFieldValuesFromStoredSettings(storedSettings: AppSettingsPreferences): HoursMinutesDaysFieldValues {
        val days =
            (storedSettings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds / 3600) / 24
        val hours =
            (storedSettings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds - days * 24 * 3600) / 3600
        val minutes =
            ((storedSettings.appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds - days * 24 * 3600) - (hours * 3600))/60
        Log.d("debugSetting", "block set lock: read value from prefs is $storedSettings")
        Log.d("debugSetting", "block set lock: hours, mins, days read from shared prefs are days: $days | hours: $hours | minutes: $minutes")
        return HoursMinutesDaysFieldValues(
            days = days.toString(),
            hours = hours.toString(),
            minutes = minutes.toString()
        )
    }

    fun getBlockListLockFieldValuesFromStoredSettings(storedSettings: AppSettingsPreferences): HoursMinutesDaysFieldValues {
        val days = (storedSettings.appBlockListLockDurationAfterAddToBlockListInSeconds / 3600) / 24
        val hours =
            (storedSettings.appBlockListLockDurationAfterAddToBlockListInSeconds - days * 24 * 3600) / 3600
        val minutes =
            ((storedSettings.appBlockListLockDurationAfterAddToBlockListInSeconds - days * 24 * 3600) - (hours * 3600))/60
        Log.d("debugSetting", "blocked app lock: read value from prefs is $storedSettings")
        Log.d("debugSetting", "blocked app lock: hours, mins, days read from shared prefs are days: $days | hours: $hours | minutes: $minutes")
        return HoursMinutesDaysFieldValues(
            days = days.toString(),
            hours = hours.toString(),
            minutes = minutes.toString()
        )
    }

    fun getDurationFromFieldValues(
        blockSetLockDaysFieldValue: String,
        blockSetLockHoursFieldValue: String,
        blockSetLockMinutesFieldValue: String,
        blockedAppLockDaysFieldValue: String,
        blockedAppLockHoursFieldValue: String,
        blockedAppLockMinutesFieldValue: String,
    ): BlockDurations {
        Log.d("debugSetting", "block set lock: days: $blockSetLockDaysFieldValue | hours: $blockSetLockHoursFieldValue | minutes: $blockSetLockMinutesFieldValue")
        Log.d("debugSetting", "blocked app lock: days: $blockedAppLockDaysFieldValue | hours: $blockedAppLockHoursFieldValue | minutes: $blockedAppLockMinutesFieldValue")
        return BlockDurations(
            blockedAppLockDurationAfterAppAddInSeconds = (blockedAppLockDaysFieldValue.toIntOrNull()
                ?: 0) * 3600 * 24 + (blockedAppLockHoursFieldValue.toIntOrNull()
                ?: 0) * 3600 + (blockedAppLockMinutesFieldValue.toIntOrNull() ?: 0) * 60,
            blockSetLockDurationAfterBlockSetCreateOrUpdateInSeconds = (blockSetLockDaysFieldValue.toIntOrNull()
                ?: 0) * 3600 * 24 + (blockSetLockHoursFieldValue.toIntOrNull()
                ?: 0) * 3600 + (blockSetLockMinutesFieldValue.toIntOrNull() ?: 0) * 60
        )
    }

    fun updateSettings(
        blockDurations: BlockDurations
    ) {
        viewModelScope.launch {
            dataStore.updateData { preferences ->
                preferences.copy(
                    appBlockListLockDurationAfterAddToBlockListInSeconds = blockDurations.blockedAppLockDurationAfterAppAddInSeconds,
                    appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds = blockDurations.blockSetLockDurationAfterBlockSetCreateOrUpdateInSeconds
                )
            }
        }
    }
}

data class HoursMinutesDaysFieldValues(val minutes: String, val hours: String, val days: String)

data class BlockDurations(
    val blockedAppLockDurationAfterAppAddInSeconds: Int,
    val blockSetLockDurationAfterBlockSetCreateOrUpdateInSeconds: Int
)