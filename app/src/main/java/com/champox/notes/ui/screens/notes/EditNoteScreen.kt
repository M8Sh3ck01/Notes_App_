package com.champox.notes.ui.screens.notes

import DeleteConfirmationDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.champox.notes.data.model.Note
import com.champox.notes.ui.components.MoreOptionsMenuButton
import com.champox.notes.ui.components.toFormattedString
import com.champox.notes.viewmodels.NoteCategory
import com.champox.notes.viewmodels.NotesViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun EditNoteScreen(
    notesViewModel: NotesViewModel,
    note: Note,
    onSaveAndBack: (String, String) -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var title by remember(note.id) { mutableStateOf(note.title) }
    var content by remember(note.id) { mutableStateOf(note.content) }
    var isEditable by remember { mutableStateOf(true) }

    val listState = rememberLazyListState()
    val colors = MaterialTheme.colorScheme

    // ✅ Auto-save after typing delay
    LaunchedEffect(title, content) {
        snapshotFlow { title to content }
            .debounce(1200L)
            .distinctUntilChanged()
            .collect { (t, c) ->
                if (t != note.title || c != note.content) {
                    notesViewModel.updateNote(note.copy(title = t, content = c))
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        // Call the passed callback with current title and content,
                        // so MainActivity handles saving and navigation
                        onSaveAndBack(title, content)
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }

                },
                actions = {
                    IconButton(onClick = {
                        // Optional: share logic
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }

                    if (isEditable) {
                        IconButton(onClick = {
                            isEditable = false
                            coroutineScope.launch {
                                notesViewModel.updateNote(note.copy(title = title, content = content))
                            }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    } else {
                        IconButton(onClick = { isEditable = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }

                        MoreOptionsMenuButton(
                            isPinned = note.isPinned,
                            isArchived = note.isArchived,
                            isFavorite = note.isFavorite,
                            onPin = { notesViewModel.setNoteCategoryExclusive(note, NoteCategory.PINNED) },
                            onArchive = { notesViewModel.setNoteCategoryExclusive(note, NoteCategory.ARCHIVED) },
                            onStar = { notesViewModel.setNoteCategoryExclusive(note, NoteCategory.FAVORITES) },
                            onDelete = { showDeleteDialog = true }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    scrolledContainerColor = colors.background,
                    navigationIconContentColor = colors.onBackground,
                    actionIconContentColor = colors.onBackground
                )
            )
        },
        containerColor = colors.background
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 120.dp,
                start = 16.dp,
                end = 16.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .imePadding()
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title", color = colors.onSurfaceVariant) },
                    enabled = isEditable,
                    textStyle = MaterialTheme.typography.titleLarge.copy(color = colors.onBackground),
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        disabledContainerColor = colors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        disabledTextColor = colors.onBackground,
                        cursorColor = colors.primary
                    )
                )

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "last modified: ${note.lastModified.toFormattedString()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            item {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = {
                        if (!isEditable) {
                            Text("(double tap to edit)", color = colors.onSurfaceVariant)
                        }
                    },
                    enabled = isEditable,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.onBackground),
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { isEditable = true })
                        },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        disabledContainerColor = colors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        disabledTextColor = colors.onBackground,
                        cursorColor = colors.primary
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(200.dp)) }
        }

        DeleteConfirmationDialog(
            visible = showDeleteDialog,
            onConfirmDelete = {
                notesViewModel.deleteNote(note)
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}
