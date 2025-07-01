package com.champox.notes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.champox.notes.data.Note
import com.champox.notes.ui.components.NoteCard
import com.champox.notes.ui.viewmodels.NotesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddNote: () -> Unit,
    onEditNote: (Int, Note) -> Unit,
    viewModel: NotesViewModel = viewModel()
) {
    val notes = viewModel.notes.sortedByDescending { it.createdAt }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Notes", style = MaterialTheme.typography.titleLarge)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 1.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = innerPadding.calculateBottomPadding() + 80.dp
            )
        ) {
            itemsIndexed(notes) { index, note ->
                NoteCard(
                    title = note.title,
                    content = note.content,
                    createdAt = note.createdAt,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    onClick = {
                        onEditNote(index, note)
                    }
                )

            }
        }
    }
}
