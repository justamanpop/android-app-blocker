package com.example.appblocker

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString
import java.io.InputStream
import java.io.OutputStream
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class AppSettingsPreferences @OptIn(ExperimentalTime::class) constructor(
    val appBlockListLockDurationAfterAddToBlockListInSeconds: Int,
    val appBlockSetLockDurationAfterCreateOrUpdateBlockSetInSeconds: Int,
)

object AppSettingsPreferencesSerializer : Serializer<AppSettingsPreferences> {
    override val defaultValue: AppSettingsPreferences = AppSettingsPreferences(120, 120)

    override suspend fun readFrom(input: InputStream): AppSettingsPreferences {
        try {
            return decodeFromString<AppSettingsPreferences>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read App Block List preferences", serialization)
        }
    }

    override suspend fun writeTo(
        t: AppSettingsPreferences,
        output: OutputStream
    ) {
        output.write(encodeToString(t).encodeToByteArray())
    }

}

@Serializable
data class AppBlockItemPreferences @OptIn(ExperimentalTime::class) constructor(
    val appName: String,
    val appPackageName: String,
    val addedAt: Instant
)

@Serializable
data class AppBlockSetPreferences @OptIn(ExperimentalTime::class) constructor(
    val id: Int,
    val name: String,
    val blockList: List<AppBlockItemPreferences>,
    val activeDays: Map<DayOfWeek, Boolean>,
    val activeTime: String,
    val lastUpdatedAt: Instant
)


object AppBlockSetPreferencesSerializer : Serializer<List<AppBlockSetPreferences>> {
    override val defaultValue: List<AppBlockSetPreferences> = listOf()

    override suspend fun readFrom(input: InputStream): List<AppBlockSetPreferences> {
        try {
            return decodeFromString<List<AppBlockSetPreferences>>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read App Block List preferences", serialization)
        }
    }

    override suspend fun writeTo(
        t: List<AppBlockSetPreferences>,
        output: OutputStream
    ) {
        output.write(encodeToString(t).encodeToByteArray())
    }

}

data class CreateBlockSetFormValidationResult(
    val nameErrorMessage: String?,
    val activeTimeErrorMessage: String?
)

fun validateBlockSetName(
    blockSetName: String,
    blockSets: List<AppBlockSetPreferences>,
    allowDuplicateName: Boolean,
): String? {
    if (blockSetName == "") {
        return "Block set name cannot be empty"
    }
    if (!allowDuplicateName && blockSets.any { bs -> bs.name == blockSetName }) {
        return "Block set with name $blockSetName already exists"
    }
    return null
}

fun validateActiveTime(
    activeTime: String,
): String? {
    if (activeTime == "") {
        return "Block time cannot be empty. Click All Day to enable it at all times"
    }

    val timeRanges = activeTime.split(",")
    val invalidTimeRangeErrorMessage = { timeRange: String -> "Time range $timeRange is invalid" }

    for (timeRange in timeRanges) {
        if (timeRange.length != 9) {
            return invalidTimeRangeErrorMessage(timeRange)
        }

        val splitValues = timeRange.split("-")
        val startTime = splitValues[0]
        val endTime = splitValues[1]
        if (splitValues.size != 2 || startTime >= endTime || startTime.length != 4 || endTime.length != 4) {
            return invalidTimeRangeErrorMessage(timeRange)
        }

        val startHour = startTime.substring(0, 2)
        val startMinute = startTime.substring(2, 4)
        if (startHour.toIntOrNull() == null || startMinute.toIntOrNull() == null) {
            return invalidTimeRangeErrorMessage(timeRange)
        }

        if (startHour.toInt() !in 0..24 || startMinute.toInt() !in 0..59) {
            return invalidTimeRangeErrorMessage(timeRange)
        }
    }
    return null
}