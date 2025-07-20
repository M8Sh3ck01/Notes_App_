@file:OptIn(ExperimentalMaterial3Api::class)

package com.champox.notes.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.champox.notes.ui.components.AddNoteFAB
import com.champox.notes.ui.components.BottomActions
import com.champox.notes.ui.components.EmptyState
import com.champox.notes.ui.components.NoteItem
import com.champox.notes.ui.components.NotesTopAppBar
import com.champox.notes.ui.components.SearchBar
import com.champox.notes.ui.theme.PureBlack
import com.champox.notes.ui.theme.PureWhite
import com.champox.notes.viewmodels.NotesViewModel

@Composable
fun HomeScreen(
    viewModel: NotesViewModel,
    onAddNote: () -> Unit,
    onNoteClick: (Long) -> Unit
) {
    val notes by viewModel.filteredNotes.collectAsState()
    val selectedNotes by viewModel.selectedNotes.collectAsState()
    val isSelectionMode by viewModel.isSelectionMode.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            NotesTopAppBar(
                isSelectionMode = isSelectionMode,
                selectedCount = selectedNotes.size,
                onCancelSelection = { viewModel.clearSelectedNotes() },
                onMenuClick = { /* Handle menu click */ },
                onViewModeClick = { /* Handle view mode toggle */ }
            )

            SearchBar(
                modifier = Modifier.padding(16.dp),
                onSearch = viewModel::setSearchQuery
            )

            Text(
                text = "All Notes",
                color = PureBlack,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (notes.isEmpty()) {
                EmptyState(onAddNote = onAddNote)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(1f)

                ) {
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            isSelected = selectedNotes.contains(note),
                            isSelectionMode = isSelectionMode,
                            onClick = {
                                if (isSelectionMode) {
                                    viewModel.toggleNoteSelection(note)
                                } else {
                                    onNoteClick(note.id)
                                }
                            },
                            onLongClick = {
                                viewModel.toggleNoteSelection(note)
                            }
                        )
                    }
                }
            }
        }

        if (isSelectionMode) {
            BottomActions(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                onShare = {
                    // TODO: implement share functionality for selectedNotes
                },
                onDelete = {
                    selectedNotes.forEach { viewModel.deleteNote(it) }
                    viewModel.clearSelectedNotes()
                },
                onSelect = {
                    if (selectedNotes.size < notes.size) {
                        viewModel.selectAllNotes(notes)
                    } else {
                        viewModel.clearSelectedNotes()
                    }
                }
            )
        } else {
            AddNoteFAB(
                onClick = onAddNote,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 10.dp)
            )
        }
    }
}
