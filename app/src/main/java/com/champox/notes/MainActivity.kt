package com.champox.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.champox.notes.data.local.AppDatabase
import com.champox.notes.data.local.session.UserSession.currentUserEmail
import com.champox.notes.data.model.Note
import com.champox.notes.data.repository.AuthRepositoryImpl
import com.champox.notes.data.repository.NoteRepository
import com.champox.notes.navigation.Screen
import com.champox.notes.navigation.Screen.*
import com.champox.notes.ui.components.WelcomeScreen
import com.champox.notes.ui.screens.SettingsScreen
import com.champox.notes.ui.screens.auth.LoginScreen
import com.champox.notes.ui.screens.auth.SignUpScreen
import com.champox.notes.ui.screens.notes.EditNoteScreen
import com.champox.notes.ui.screens.notes.HomeScreen
import com.champox.notes.ui.theme.NotesTheme
import com.champox.notes.viewmodels.AuthViewModel
import com.champox.notes.viewmodels.NotesViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val noteRepository = NoteRepository(database.noteDao())
        val authRepository = AuthRepositoryImpl(database.userDao())

        setContent {
            NotesTheme(darkTheme = isSystemInDarkTheme()) {
                val notesViewModel: NotesViewModel = viewModel(
                    factory = NotesViewModel.provideFactory(noteRepository)
                )
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModel.provideFactory(authRepository)
                )

                val coroutineScope = rememberCoroutineScope()

                var showWelcome by remember { mutableStateOf(false) }
                var isLoading by remember { mutableStateOf(true) }

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
                val selectedNote by notesViewModel.selectedNote.collectAsState()

                // Check for existing users
                LaunchedEffect(Unit) {
                    val hasAccounts = authRepository.hasAnyAccounts()
                    showWelcome = !hasAccounts
                    currentScreen = if (hasAccounts) Screen.Login else Screen.Welcome
                    isLoading = false
                }

                // Navigate to Home on successful login
                LaunchedEffect(authViewModel.isAuthenticated) {
                    authViewModel.isAuthenticated.collect { isAuthenticated ->
                        if (isAuthenticated) {
                            currentScreen = Screen.Home
                        }
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    when (currentScreen) {
                        Screen.Welcome -> WelcomeScreen(
                            onGetStarted = { currentScreen = Screen.SignUp }
                        )

                        Screen.Login -> LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = { currentScreen = Screen.Home },
                            onNavigateToRegister = { currentScreen = Screen.SignUp }
                        )

                        Screen.SignUp -> SignUpScreen(
                            viewModel = authViewModel,
                            onRegisterSuccess = { currentScreen = Screen.Home },
                            onNavigateToLogin = { currentScreen = Screen.Login }
                        )

                        is Screen.Home -> HomeScreen(
                            viewModel = notesViewModel,
                            onAddNote = {
                                coroutineScope.launch {
                                    val newNote = Note(
                                        title = "", content = "",
                                        userId = currentUserEmail.toString()
                                    )
                                    val newId = notesViewModel.addNote(newNote)
                                    notesViewModel.loadNoteById(newId) // ✅ Load before navigation
                                    currentScreen = EditNote(newId)
                                }
                            },
                            onNoteClick = { noteId ->
                                coroutineScope.launch {
                                    notesViewModel.loadNoteById(noteId) // ✅ Load before navigation
                                    currentScreen = EditNote(noteId)
                                }
                            },
                            onSignOut = {
                                authViewModel.logout()
                                currentScreen = if (showWelcome) Screen.Welcome else Screen.Login
                            },
                            onSettingsClick = {
                                currentScreen = Screen.Settings
                            }
                        )

                        is Screen.EditNote -> {
                            if (selectedNote == null) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                EditNoteScreen(
                                    notesViewModel = notesViewModel,
                                    note = selectedNote!!,
                                    onSaveAndBack = { title, content ->
                                        coroutineScope.launch {
                                            if (title.isBlank() && content.isBlank()) {
                                                notesViewModel.deleteNote(selectedNote!!)
                                            } else {
                                                if (title.isBlank()) {
                                                    title.trim() == "Untitled"
                                                }
                                                val updatedNote = selectedNote!!.copy(

                                                    title = title.trim(),

                                                )
                                                notesViewModel.updateNote(updatedNote)
                                            }
                                            currentScreen = Screen.Home
                                        }
                                    },
                                    onDelete = {
                                        coroutineScope.launch {
                                            notesViewModel.deleteNote(selectedNote!!)
                                            currentScreen = Screen.Home
                                        }
                                    },
                                    onShare = { /* TODO: Share note */ }
                                )
                            }
                        }

                        Screen.Settings -> SettingsScreen(

                            onBack = {currentScreen = Screen.Home}
                        )
                    }
                }
            }
        }
    }
}//b8
