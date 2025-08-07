@file:OptIn(ExperimentalMaterial3Api::class)

package com.champox.notes.ui.screens.notes

//noinspection UsingMaterialAndMaterial3Libraries
import DeleteConfirmationDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.champox.notes.ui.components.AddNoteFAB
import com.champox.notes.ui.components.BottomActions
import com.champox.notes.ui.components.EmptyState
import com.champox.notes.ui.components.NoteCategorySelector
import com.champox.notes.ui.components.NoteItem
import com.champox.notes.ui.components.NotesAppDrawer
import com.champox.notes.ui.components.NotesTopAppBar
import com.champox.notes.ui.preferences.FilterPreferences
import com.champox.notes.ui.screens.search.SearchScreen
import com.champox.notes.viewmodels.NoteCategory
import com.champox.notes.viewmodels.NotesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    filterPreferences: FilterPreferences, // Add this parameter
    viewModel: NotesViewModel,
    onAddNote: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onSignOut: () -> Unit,
    onSettingsClick: () -> Unit,
) {

    val isFilterActive by filterPreferences.filterVisibleFlow.collectAsState(initial = true)

    var showDeleteDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val notes by viewModel.filteredNotes.collectAsState()
    val selectedNoteIds by viewModel.selectedNoteIds.collectAsState()
    val isSelectionMode by viewModel.isSelectionMode.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NotesAppDrawer(
                selectedCategory = selectedCategory.name,
                onCategorySelected = { category ->
                    viewModel.setCategory(NoteCategory.valueOf(category))
                    scope.launch { drawerState.close() }
                },
                onSignOut = onSignOut,
                onSettingsClick = onSettingsClick,
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main notes list + UI
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                NotesTopAppBar(
                    isFilterActive = isFilterActive,
                    isSelectionMode = isSelectionMode,
                    selectedCount = selectedNoteIds.size,
                    onCancelSelection = { viewModel.clearSelectedNotes() },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onViewModeClick = {  coroutineScope.launch {
                        filterPreferences.setFilterVisible(!isFilterActive)
                    } },
                    onSearchClick = {
                        if (!isSelectionMode) {
                            viewModel.openSearch()
                        }
                    },
                    onSelect = {
                            viewModel.toggleSelectAllVisibleNotes(notes)
                    },
                )

                AnimatedVisibility(
                    visible = isFilterActive && !isSelectionMode,
                    enter = fadeIn(), exit = fadeOut()
                ) {
                    Column {
                        NoteCategorySelector(
                            selected = selectedCategory,
                            onSelect = { category -> viewModel.setCategory(category) },
                            onBeforeSelect = { viewModel.clearSelectedNotes() },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = when (selectedCategory) {
                                NoteCategory.ALL -> "All Notes"
                                NoteCategory.PINNED -> "Pinned Notes"
                                NoteCategory.ARCHIVED -> "Archived Notes"
                                NoteCategory.FAVORITES -> "Favorite Notes"
                            },
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 1.dp)
                        )
                    }
                }
                if (!isFilterActive) {
                    Text(
                        text = when (selectedCategory) {
                            NoteCategory.ALL -> "All Notes"
                            NoteCategory.PINNED -> "Pinned Notes"
                            NoteCategory.ARCHIVED -> "Archived Notes"
                            NoteCategory.FAVORITES -> "Favorite Notes"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 1.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (notes.isEmpty()) {
                    EmptyState(onAddNote = onAddNote)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(
                            bottom = 300.dp,
                            top = 1.dp
                        )
                    ) {
                        itemsIndexed(notes) { index, note ->
                            val isFirst = index == 0
                            val isLast = index == notes.lastIndex

                            NoteItem(
                                note = note,
                                isSelected = selectedNoteIds.contains(note.id),
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
                                },
                                shape = when {
                                    isFirst -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                    isLast -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                                    else -> RoundedCornerShape(0.dp)
                                }
                            )

                            if (!isLast) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Divider(
                                        color = MaterialTheme.colorScheme.secondary,
                                        thickness = 0.5.dp,
                                        modifier = Modifier.fillMaxWidth(0.85f)
                                    )
                                }
                            }
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
                    isArchived = selectedNoteIds.isNotEmpty() &&
                            notes.filter { selectedNoteIds.contains(it.id) }.all { it.isArchived },
                    isPinned = selectedNoteIds.isNotEmpty() &&
                            notes.filter { selectedNoteIds.contains(it.id) }.all { it.isPinned },
                    isFavorite = selectedNoteIds.isNotEmpty() &&
                            notes.filter { selectedNoteIds.contains(it.id) }.all { it.isFavorite },
                    isLoading = false, // Set this based on your loading state
                    onArchive = {
                        if (selectedNoteIds.isNotEmpty()) {
                            val allArchived = notes.filter { selectedNoteIds.contains(it.id) }.all { it.isArchived }
                            if (allArchived) {
                                viewModel.unarchiveSelectedNotes()
                            } else {
                                viewModel.archiveSelectedNotes()
                            }
                        }
                    },
                    onDelete = {
                        if (selectedNoteIds.isNotEmpty()) {
                            showDeleteDialog = true
                        }
                    },

                    onPin = {
                        if (selectedNoteIds.isNotEmpty()) {
                            val allPinned = notes.filter { selectedNoteIds.contains(it.id) }.all { it.isPinned }
                            if (allPinned) {
                                viewModel.unpinSelectedNotes()
                            } else {
                                viewModel.pinSelectedNotes()
                            }
                        }
                    },
                    onStar = {
                        if (selectedNoteIds.isNotEmpty()) {
                            val allStarred = notes.filter { selectedNoteIds.contains(it.id) }.all { it.isFavorite }
                            if (allStarred) {
                                viewModel.unstarSelectedNotes()
                            } else {
                                viewModel.starSelectedNotes()
                            }
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

            // Fullscreen search overlay
            AnimatedVisibility(
                visible = isSearchActive,
                modifier = Modifier.fillMaxSize(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    SearchScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.closeSearch() },
                        onNoteClick = { id ->
                            onNoteClick(id)
                            viewModel.closeSearch()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        DeleteConfirmationDialog(
            visible = showDeleteDialog,
            itemCount = selectedNoteIds.size,
            onConfirmDelete = {
                val selectedNotes = notes.filter { selectedNoteIds.contains(it.id) }
                selectedNotes.forEach { viewModel.deleteNote(it) }
                viewModel.clearSelectedNotes()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}
