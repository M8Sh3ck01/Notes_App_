package com.champox.notes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.champox.notes.ui.theme.AppThemeMode
import com.champox.notes.ui.theme.ThemePreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val preferenceManager: ThemePreferenceManager
) : ViewModel() {

    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode

    init {
        preferenceManager.themeModeFlow
            .onEach { mode -> _themeMode.value = mode }
            .launchIn(viewModelScope)
    }

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            preferenceManager.saveThemeMode(mode)
        }
    }
}





class ThemeViewModelFactory(
    private val preferenceManager: ThemePreferenceManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(preferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

