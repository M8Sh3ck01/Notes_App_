package com.champox.notes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.champox.notes.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object LoggedOut : AuthState()
    object LoggedIn : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val success = authRepository.login(username, password)
            _authState.value = if (success) AuthState.LoggedIn else AuthState.Error("Invalid credentials")
        }
    }

    fun signUp(username: String, password: String) {
        viewModelScope.launch {
            val success = authRepository.signUp(username, password)
            _authState.value = if (success) AuthState.LoggedIn else AuthState.Error("User already exists")
        }
    }

    fun logout() {
        _authState.value = AuthState.LoggedOut
    }
} 