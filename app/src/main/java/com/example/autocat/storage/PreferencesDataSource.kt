package com.example.autocat.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.autocat.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


interface PreferencesDataSource {
    val userFlow: Flow<User>

    suspend fun updateUser(user: User? = null)
}

class PreferencesStoreDataSource(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : PreferencesDataSource {
    companion object {
        val USER_KEY = stringPreferencesKey("user_key")
    }

    //TODO handle errors
    override val userFlow: Flow<User> = dataStore.data
        .map { preferences ->
            val userStr = preferences[USER_KEY] ?: "{}"
            json.decodeFromString<User>(userStr)
        }

    override suspend fun updateUser(user: User?) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[USER_KEY] = user?.let { json.encodeToString(it) } ?: "{}"
            }
        }
    }
}