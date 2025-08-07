package com.champox.notes.viewmodels

import androidx.lifecycle.ViewModel
import com.champox.notes.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NavigationViewModel : ViewModel() {

    // Backing state flow for navigation stack
    private val _navigationStack = MutableStateFlow<List<Screen>>(listOf(Screen.Welcome))
    val navigationStack: StateFlow<List<Screen>> = _navigationStack

    // Current screen derived from the last screen in the stack
    private val _currentScreen = MutableStateFlow<Screen>(_navigationStack.value.last())
    val currentScreen: StateFlow<Screen> = _currentScreen

    // Navigate to a new screen by adding it to the stack
    fun navigateTo(screen: Screen) {
        _navigationStack.value = _navigationStack.value + screen
        updateCurrentScreen()
    }

    // Navigate back by removing the last screen if possible
    fun navigateBack() {
        if (_navigationStack.value.size > 1) {
            _navigationStack.value = _navigationStack.value.dropLast(1)
            updateCurrentScreen()
        }
    }

    // Reset the navigation stack to a single screen
    fun resetTo(screen: Screen) {
        _navigationStack.value = listOf(screen)
        updateCurrentScreen()
    }

    // Update the currentScreen flow to the last screen in the stack
    private fun updateCurrentScreen() {
        _currentScreen.value = _navigationStack.value.last()
    }
}
