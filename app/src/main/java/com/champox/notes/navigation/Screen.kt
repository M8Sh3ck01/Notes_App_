package com.champox.notes.navigation

sealed class Screen {
    object Home : Screen()
    data class EditNote(val noteId: Long) : Screen()
}






