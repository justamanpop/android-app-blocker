package com.example.appblocker

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class AppBlockListPreferences(val appName: String, val appPackageName: String)

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