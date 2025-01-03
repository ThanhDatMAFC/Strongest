package com.example.strongest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.strongest.DataApplication
import com.example.strongest.data.repo.UserPreferenceRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UserPrefUiState(val isLogin: Boolean = false)

class UserViewModel(private val userPreferenceRepo: UserPreferenceRepo): ViewModel() {
    val userState: StateFlow<UserPrefUiState> = userPreferenceRepo.userInfo
        .map { userInfo -> UserPrefUiState(userInfo) }
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = UserPrefUiState()
        )

    fun setAuthState(state: Boolean) {
        if (userState.value.isLogin == state) return
        Log.d(TAG, "Set auth state: $state")
        viewModelScope.launch {
            userPreferenceRepo.isUserLogIn(state)
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DataApplication)
                UserViewModel(application.userPreferenceRepo)
            }
        }
        val TAG = "USER_DATA_STORE"
    }

}