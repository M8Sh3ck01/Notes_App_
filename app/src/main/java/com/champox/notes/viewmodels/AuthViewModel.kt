// viewmodels/AuthViewModel.kt
package com.champox.notes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.champox.notes.data.local.session.UserSession
import com.champox.notes.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                if (repository.register(email, password)) {
                    UserSession.currentUserEmail = email // ✅ Store the email

                    _isAuthenticated.value = true
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Email already registered")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                if (repository.login(email, password)) {
                    UserSession.currentUserEmail = email // ✅ Store the email
                    _isAuthenticated.value = true
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        _isAuthenticated.value = false
        _authState.value = AuthState.Idle
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }


    companion object {
        fun provideFactory(repository: AuthRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(repository) as T
                }
            }
        }
    }
}///////////