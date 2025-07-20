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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    private val _selectedNotes = MutableStateFlow<List<Note>>(emptyList())
    val selectedNotes: StateFlow<List<Note>> = _selectedNotes

    val isSelectionMode: StateFlow<Boolean> = selectedNotes
        .map { it.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val filteredNotes = combine(
        repository.getAllNotes(),
        _searchQuery
    ) { notes, query ->
        if (query.isBlank()) notes
        else notes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- NEW: Selected Note state ---
    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote

    fun loadNoteById(id: Long) {
        viewModelScope.launch {
            val note = repository.getNoteById(id)
            _selectedNote.value = note
        }
    }

    fun clearSelectedNote() {
        _selectedNote.value = null
    }
    // --- End selectedNote ---

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleNoteSelection(note: Note) {
        val current = _selectedNotes.value.toMutableList()
        if (current.contains(note)) current.remove(note) else current.add(note)
        _selectedNotes.value = current
    }

    fun clearSelectedNotes() {
        _selectedNotes.value = emptyList()
    }

    fun selectAllNotes(notes: List<Note>) {
        _selectedNotes.value = notes
    }

    suspend fun addNote(note: Note): Long {
        val fresh = note.copy(lastModified = Date())
        return repository.insertNote(fresh)
    }

    suspend fun getNoteById(id: Long): Note? {
        return repository.getNoteById(id)
    }

    // Updated to suspend function for proper coroutine control
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
