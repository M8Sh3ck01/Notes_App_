package com.champox.notes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.champox.notes.data.model.Note
import com.champox.notes.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

enum class NoteCategory {
    ALL, PINNED, ARCHIVED, FAVORITES
}

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    // States
    private val _mainQuery = MutableStateFlow("")
    private val _selectedNoteIds = MutableStateFlow<List<Long>>(emptyList())
    private val _selectedCategory = MutableStateFlow(NoteCategory.ALL)
    private val _selectedNote = MutableStateFlow<Note?>(null)

    private val _searchQuery = MutableStateFlow("")
    private val _searchResults = MutableStateFlow<List<Note>>(emptyList())
    private val _isSearchActive = MutableStateFlow(false)

    val selectedNoteIds: StateFlow<List<Long>> = _selectedNoteIds
    val selectedCategory: StateFlow<NoteCategory> = _selectedCategory
    val selectedNote: StateFlow<Note?> = _selectedNote
    val searchResults: StateFlow<List<Note>> = _searchResults
    val isSearchActive: StateFlow<Boolean> = _isSearchActive

    val isSelectionMode: StateFlow<Boolean> = selectedNoteIds
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val allNotesFlow = flow {
        val userEmail = com.champox.notes.data.local.session.UserSession.currentUserEmail
        if (userEmail != null) {
            emitAll(repository.getUserNotes(userEmail))
        } else {
            emit(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val filteredNotes = combine(
        allNotesFlow, _mainQuery, _selectedCategory
    ) { notes, query, category ->
        val searched = if (query.isBlank()) notes else notes.filter {
            it.title.contains(query, true) || it.content.contains(query, true)
        }
        when (category) {
            NoteCategory.ALL -> searched.filter { !it.isArchived }
            NoteCategory.PINNED -> searched.filter { it.isPinned && !it.isArchived }
            NoteCategory.ARCHIVED -> searched.filter { it.isArchived }
            NoteCategory.FAVORITES -> searched.filter { it.isFavorite && !it.isArchived }
        }
    }
        .debounce(200) // debounce to avoid rapid emissions after archive/unarchive
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    // 1. Sharing state
    private val _shareNoteEvent = MutableStateFlow<String?>(null)
    val shareNoteEvent: StateFlow<String?> = _shareNoteEvent

    // 2. Trigger sharing
    fun requestNoteShare(note: Note) {
        val content = buildString {
            append("📝 ${note.title}\n\n")
            append(note.content)
        }
        _shareNoteEvent.value = content
    }

    // 3. Clear after handled
    fun onNoteShared() {
        _shareNoteEvent.value = null
    }






    // Search
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _searchResults.value = if (query.isBlank()) {
                emptyList()
            } else {
                allNotesFlow.value.filter {
                    it.title.contains(query, true) || it.content.contains(query, true)
                }
            }
        }
    }

    fun openSearch() {
        _isSearchActive.value = true
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun closeSearch() {
        _isSearchActive.value = false
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun setMainQuery(query: String) {
        _mainQuery.value = query
    }

    fun setCategory(category: NoteCategory) {
        _selectedCategory.value = category
    }

    fun setNoteCategoryExclusive(note: Note, category: NoteCategory) {
        viewModelScope.launch {
            val updated = when (category) {
                NoteCategory.PINNED -> {
                    val newPinned = !note.isPinned
                    note.copy(
                        isPinned = newPinned,
                        isArchived = false,
                        isFavorite = false,
                        lastModified = Date()
                    )
                }
                NoteCategory.ARCHIVED -> {
                    val newArchived = !note.isArchived
                    note.copy(
                        isArchived = newArchived,
                        isPinned = false,
                        isFavorite = false,
                        lastModified = Date()
                    )
                }
                NoteCategory.FAVORITES -> {
                    val newFavorite = !note.isFavorite
                    note.copy(
                        isFavorite = newFavorite,
                        isPinned = false,
                        isArchived = false,
                        lastModified = Date()
                    )
                }
                NoteCategory.ALL -> {
                    note.copy(
                        isPinned = false,
                        isArchived = false,
                        isFavorite = false,
                        lastModified = Date()
                    )
                }
            }

            repository.updateNote(updated)
            if (_selectedNote.value?.id == note.id) {
                _selectedNote.value = updated
            }
            if (updated.isArchived) {
                _selectedNoteIds.value = _selectedNoteIds.value.filter { it != note.id }
            }
        }
    }

    fun loadNoteById(id: Long) {
        viewModelScope.launch {
            _selectedNote.value = repository.getNoteById(id)
        }
    }

    fun clearSelectedNote() {
        _selectedNote.value = null
    }

    fun unarchiveSelectedNotes() {
        viewModelScope.launch {
            val currentSelectedIds = _selectedNoteIds.value
            val allNotes = allNotesFlow.value
            val currentSelectedNotes = allNotes.filter { currentSelectedIds.contains(it.id) }
            currentSelectedNotes.forEach { note ->
                val updatedNote = note.copy(
                    isArchived = false,
                    lastModified = Date()
                )
                repository.updateNote(updatedNote)
            }
            clearSelectedNotes()
        }
    }

    fun archiveSelectedNotes() {
        viewModelScope.launch {
            val currentSelectedIds = _selectedNoteIds.value
            val allNotes = allNotesFlow.value
            val currentSelectedNotes = allNotes.filter { currentSelectedIds.contains(it.id) }
            currentSelectedNotes.forEach { note ->
                val updatedNote = note.copy(
                    isArchived = true,
                    isPinned = false,
                    isFavorite = false,
                    lastModified = Date()
                )
                repository.updateNote(updatedNote)
            }
            clearSelectedNotes()
        }
    }

    fun toggleNoteSelection(note: Note) {
        val current = _selectedNoteIds.value.toMutableList()
        if (current.contains(note.id)) current.remove(note.id) else current.add(note.id)
        _selectedNoteIds.value = current
    }
    fun toggleSelectAllVisibleNotes(notes: List<Note>) {
        val currentSelected = _selectedNoteIds.value
        if (currentSelected.size < notes.size) {
            // Select all
            _selectedNoteIds.value = notes.map { it.id }
        } else {
            // Deselect all
            _selectedNoteIds.value = emptyList()
        }
    }



    fun clearSelectedNotes() {
        _selectedNoteIds.value = emptyList()
    }

    fun selectAllNotes(notes: List<Note>) {
        _selectedNoteIds.value = notes.map { it.id }
    }
    fun unselectNotes(notesToUnselect: List<Note>) {
        val currentSelected = _selectedNoteIds.value.toMutableList()
        val idsToUnselect = notesToUnselect.map { it.id }
        currentSelected.removeAll(idsToUnselect)
        _selectedNoteIds.value = currentSelected
    }



    suspend fun addNote(note: Note): Long {
        return repository.insertNote(note.copy(lastModified = Date()))
    }

    suspend fun getNoteById(id: Long): Note? = repository.getNoteById(id)

    suspend fun updateNote(note: Note) {
        val updated = note.copy(lastModified = Date())
        repository.updateNote(updated)
        _selectedNote.value = updated
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    /////new
    // Add these methods to your NotesViewModel class

    fun pinSelectedNotes() {
        viewModelScope.launch {
            val currentSelectedIds = _selectedNoteIds.value
            val allNotes = allNotesFlow.value
            val currentSelectedNotes = allNotes.filter { currentSelectedIds.contains(it.id) }
            currentSelectedNotes.forEach { note ->
                val updatedNote = note.copy(
                    isPinned = true,
                    isArchived = false,
                    isFavorite = false,
                    lastModified = Date()
                )
                repository.updateNote(updatedNote)
            }
            clearSelectedNotes()
        }
    }

    fun unpinSelectedNotes() {
        viewModelScope.launch {
            val currentSelectedIds = _selectedNoteIds.value
            val allNotes = allNotesFlow.value
            val currentSelectedNotes = allNotes.filter { currentSelectedIds.contains(it.id) }
            currentSelectedNotes.forEach { note ->
                val updatedNote = note.copy(
                    isPinned = false,
                    lastModified = Date()
                )
                repository.updateNote(updatedNote)
            }
            clearSelectedNotes()
        }
    }

    fun starSelectedNotes() {
        viewModelScope.launch {
            val currentSelectedIds = _selectedNoteIds.value
            val allNotes = allNotesFlow.value
            val currentSelectedNotes = allNotes.filter { currentSelectedIds.contains(it.id) }
            currentSelectedNotes.forEach { note ->
                val updatedNote = note.copy(
                    isFavorite = true,
                    isPinned = false,
                    isArchived = false,
                    lastModified = Date()
                )
                repository.updateNote(updatedNote)
            }
            clearSelectedNotes()
        }
    }

    fun unstarSelectedNotes() {
        viewModelScope.launch {
            val currentSelectedIds = _selectedNoteIds.value
            val allNotes = allNotesFlow.value
            val currentSelectedNotes = allNotes.filter { currentSelectedIds.contains(it.id) }
            currentSelectedNotes.forEach { note ->
                val updatedNote = note.copy(
                    isFavorite = false,
                    lastModified = Date()
                )
                repository.updateNote(updatedNote)
            }
            clearSelectedNotes()
        }
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            val currentSelectedIds = _selectedNoteIds.value
            val allNotes = allNotesFlow.value
            val currentSelectedNotes = allNotes.filter { currentSelectedIds.contains(it.id) }
            currentSelectedNotes.forEach { note ->
                repository.deleteNote(note)
            }
            clearSelectedNotes()
        }
    }








    companion object {
        fun provideFactory(repository: NoteRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(repository) as T
                }
            }
        }
    }
}
