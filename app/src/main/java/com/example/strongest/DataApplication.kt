package com.example.strongest

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.strongest.data.repo.UserPreferenceRepo

private const val USER_DATA = "user_data"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATA)

class DataApplication: Application() {
    lateinit var userPreferenceRepo: UserPreferenceRepo

    override fun onCreate() {
        super.onCreate()

        userPreferenceRepo = UserPreferenceRepo(dataStore)
    }
}