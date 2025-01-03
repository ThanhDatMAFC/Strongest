package com.example.strongest.data.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferenceRepo(private val dataStore: DataStore<Preferences>) {
    val userInfo: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading user information", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference -> preference[USER_DATA] ?: false }

    companion object {
        val USER_DATA = booleanPreferencesKey("login")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun isUserLogIn(v: Boolean) {
        dataStore.edit { preference -> preference[USER_DATA] = v }
    }
}