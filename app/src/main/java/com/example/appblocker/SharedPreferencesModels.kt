package com.example.appblocker

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString
import java.io.InputStream
import java.io.OutputStream
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class AppBlockItemPreferences @OptIn(ExperimentalTime::class) constructor(val appName: String, val appPackageName: String, val addedAt: Instant)

@Serializable
data class AppBlockSetPreferences @OptIn(ExperimentalTime::class) constructor(val id: Int, val name: String, val blockList: List<AppBlockItemPreferences>)


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