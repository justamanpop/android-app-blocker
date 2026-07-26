package com.example.appblocker

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString
import java.io.InputStream
import java.io.OutputStream
import java.util.Date
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class AppBlockListPreferences @OptIn(ExperimentalTime::class) constructor(val appName: String, val appPackageName: String, val addedAt: Instant)

object AppBlockListPreferencesSerializer: Serializer<List<AppBlockListPreferences>> {
    override val defaultValue: List<AppBlockListPreferences> = listOf()

    override suspend fun readFrom(input: InputStream): List<AppBlockListPreferences> {
        try {
            return decodeFromString<List<AppBlockListPreferences>>(input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read App Block List preferences", serialization)
        }
    }

    override suspend fun writeTo(
        t: List<AppBlockListPreferences>,
        output: OutputStream
    ) {
        output.write(encodeToString(t).encodeToByteArray())
    }

}

@Serializable
data class AppBlockSetPreferences @OptIn(ExperimentalTime::class) constructor(val id: Int, val name: String, val blockList: List<AppBlockListPreferences>)


object AppBlockSetPreferencesSerializer: Serializer<List<AppBlockSetPreferences>> {
    override val defaultValue: List<AppBlockSetPreferences> = listOf()

    override suspend fun readFrom(input: InputStream): List<AppBlockSetPreferences> {
        try {
            return decodeFromString<List<AppBlockSetPreferences>>(input.readBytes().decodeToString())
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