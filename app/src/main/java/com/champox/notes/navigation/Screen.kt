// navigation/Screen.kt
package com.champox.notes.navigation

sealed class Screen {
    object Welcome : Screen()
    object Login : Screen()
    object SignUp : Screen()
    object Home : Screen()
    data class EditNote(val noteId: Long) : Screen()
    object Settings: Screen()

}