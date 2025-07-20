package com.champox.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.champox.notes.data.local.AppDatabase
import com.champox.notes.data.model.Note
import com.champox.notes.data.repository.NoteRepository
import com.champox.notes.navigation.Screen
import com.champox.notes.ui.screens.notes.EditNoteScreen
import com.champox.notes.ui.screens.notes.HomeScreen
import com.champox.notes.ui.theme.NotesTheme
import com.champox.notes.viewmodels.NotesViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room DB and repository
        val database = AppDatabase.getInstance(this)
        val repository = NoteRepository(database.noteDao())
        val factory = NotesViewModel.provideFactory(repository)

        setContent {
            NotesTheme(darkTheme = isSystemInDarkTheme()) {
                val viewModel: NotesViewModel = viewModel(factory = factory)
                val coroutineScope = rememberCoroutineScope()

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
                val selectedNote by viewModel.selectedNote.collectAsState()

                // Load note by ID when navigating to EditNote
                LaunchedEffect(currentScreen) {
                    when (val screen = currentScreen) {
                        is Screen.EditNote -> {
                            if (screen.noteId > 0L) {
                                viewModel.loadNoteById(screen.noteId)
                            } else {
                                viewModel.clearSelectedNote()
                            }
                        }
                        is Screen.Home -> {
                            viewModel.clearSelectedNote()
                        }
                    }
                }

                // Navigation-based screen rendering
                when (val screen = currentScreen) {
                    is Screen.Home -> HomeScreen(
                        viewModel = viewModel,
                        onAddNote = {
                            coroutineScope.launch {
                                val newNote = Note(title = "Untitled", content = "")
                                val newId = viewModel.addNote(newNote)
                                currentScreen = Screen.EditNote(newId)
                            }
                        },
                        onNoteClick = { noteId ->
                            currentScreen = Screen.EditNote(noteId)
                        }
                    )

                    is Screen.EditNote -> {
                        if (selectedNote == null) {
                            // Themed loading UI
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Loading note...",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            EditNoteScreen(
                                note = selectedNote!!,
                                onSaveAndBack = { title, content ->
                                    coroutineScope.launch {
                                        if (title.isBlank() && content.isBlank()) {
                                            // Optional: delete empty notes
                                            // viewModel.deleteNote(selectedNote!!)
                                        } else {
                                            val updatedNote = selectedNote!!.copy(
                                                title = title.trim(),
                                                content = content.trim()
                                            )
                                            viewModel.updateNote(updatedNote)
                                        }
                                        currentScreen = Screen.Home
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
