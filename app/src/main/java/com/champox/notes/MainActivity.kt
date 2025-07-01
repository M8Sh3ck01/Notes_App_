package com.champox.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.champox.notes.data.Note
import com.champox.notes.ui.screens.EditNoteScreen
import com.champox.notes.ui.screens.HomeScreen
import com.champox.notes.ui.theme.NotesTheme
import com.champox.notes.ui.viewmodels.NotesViewModel

sealed class Screen {
    object Home : Screen()
    data class Edit(val index: Int?, val note: Note) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesTheme {
                val viewModel: NotesViewModel = viewModel()
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val screen = currentScreen) {
                        is Screen.Home -> HomeScreen(
                            onAddNote = {
                                // Open Edit screen for new note (index = null)
                                currentScreen = Screen.Edit(null, Note(title = "", content = ""))
                            },
                            onEditNote = { index, note ->
                                // Open Edit screen for existing note
                                currentScreen = Screen.Edit(index, note)
                            },
                            viewModel = viewModel
                        )

                        is Screen.Edit -> EditNoteScreen(
                            note = screen.note,
                            onSaveAndBack = { title, content ->
                                val updatedNote = Note(title = title, content = content)
                                if (screen.index == null) {
                                    viewModel.addNote(updatedNote)
                                } else {
                                    viewModel.updateNote(screen.index, updatedNote)
                                }
                                currentScreen = Screen.Home
                            }
                        )
                    }
                }
            }
        }
    }
}
