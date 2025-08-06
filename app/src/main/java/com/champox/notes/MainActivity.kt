


package com.champox.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.champox.notes.ui.theme.AppThemeMode
import com.champox.notes.ui.theme.NotesTheme
import com.champox.notes.ui.theme.ThemePreferenceManager
import com.champox.notes.viewmodels.AuthViewModel
import com.champox.notes.viewmodels.NavigationViewModel
import com.champox.notes.viewmodels.NotesViewModel
import com.champox.notes.viewmodels.ThemeViewModel
import com.champox.notes.viewmodels.ThemeViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.champox.notes.ui.components.LogoutConfirmationDialog


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val database = AppDatabase.getInstance(this)
        val noteRepository = NoteRepository(database.noteDao())
        val authRepository = AuthRepositoryImpl(database.userDao())

        setContent {
            var showLogoutDialog by remember { mutableStateOf(false) }

            val context = LocalContext.current
            val themeViewModel: ThemeViewModel = viewModel(
                factory = ThemeViewModelFactory(ThemePreferenceManager(context))
            )
            val navigationViewModel: NavigationViewModel = viewModel()
            val notesViewModel: NotesViewModel = viewModel(
                factory = NotesViewModel.provideFactory(noteRepository)
            )
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.provideFactory(authRepository)
            )

            val themeMode by themeViewModel.themeMode.collectAsState()
            val isDarkTheme = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            NotesTheme(darkTheme = isDarkTheme) {
                val coroutineScope = rememberCoroutineScope()

                // Observe navigation state
                val navigationStack by navigationViewModel.navigationStack.collectAsState()
                val currentScreen by navigationViewModel.currentScreen.collectAsState()

                // Back handler uses navigationViewModel
                BackHandler(enabled = navigationStack.size > 1) {
                    navigationViewModel.navigateBack()
                }

                // Check for existing accounts on launch
                var showWelcome by remember { mutableStateOf(false) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    val hasAccounts = authRepository.hasAnyAccounts()
                    showWelcome = !hasAccounts

                    // Only reset navigation if it's the first launch
                    if (navigationViewModel.navigationStack.value.size == 1 &&
                        navigationViewModel.navigationStack.value.first() == Screen.Welcome
                    ) {
                        navigationViewModel.resetTo(if (hasAccounts) Screen.Login else Screen.Welcome)
                    }

                    isLoading = false
                }


                // React to theme mode changes:


                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (showLogoutDialog) {
                        LogoutConfirmationDialog(
                            onConfirm = {
                                authViewModel.logout()
                                navigationViewModel.resetTo(if (showWelcome) Screen.Welcome else Screen.Login)
                                showLogoutDialog = false
                            },
                            onDismiss = { showLogoutDialog = false }
                        )
                    }

                    when (val screen = currentScreen) {
                        Screen.Welcome -> WelcomeScreen(
                            onGetStarted = { navigationViewModel.navigateTo(Screen.SignUp) }
                        )
                        Screen.Login -> LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = { navigationViewModel.resetTo(Screen.Home) },
                            onNavigateToRegister = { navigationViewModel.navigateTo(Screen.SignUp) }
                        )
                        Screen.SignUp -> SignUpScreen(
                            viewModel = authViewModel,
                            onRegisterSuccess = { navigationViewModel.resetTo(Screen.Home) },
                            onNavigateToLogin = { navigationViewModel.resetTo(Screen.Login) }
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
                                    notesViewModel.loadNoteById(newId)
                                    navigationViewModel.navigateTo(Screen.EditNote(newId))
                                }
                            },
                            onNoteClick = { noteId ->
                                coroutineScope.launch {
                                    notesViewModel.loadNoteById(noteId)
                                    navigationViewModel.navigateTo(Screen.EditNote(noteId))
                                }
                            },
                            onSignOut = {
                                showLogoutDialog = true
                            }
                            ,
                            onSettingsClick = {
                                navigationViewModel.navigateTo(Screen.Settings)
                            }
                        )
                        is Screen.EditNote -> {
                            val selectedNote by notesViewModel.selectedNote.collectAsState()
                            if (selectedNote == null) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
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
                                                val updatedNote = selectedNote!!.copy(
                                                    title = if (title.isBlank()) "Untitled" else title.trim(),
                                                    content = content.trim()
                                                )
                                                notesViewModel.updateNote(updatedNote)
                                            }
                                            navigationViewModel.navigateBack()
                                        }
                                    },
                                    onDelete = {
                                        coroutineScope.launch {
                                            notesViewModel.deleteNote(selectedNote!!)
                                            navigationViewModel.navigateBack()
                                        }
                                    },
                                )
                            }
                        }
                        Screen.Settings -> SettingsScreen(
                            onBack = { navigationViewModel.navigateBack() },
                            themeViewModel = themeViewModel
                        )
                    }
                }
            }
        }
    }
}
/////11