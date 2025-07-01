package com.champox.notes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.champox.notes.data.Note

class NotesViewModel : ViewModel() {

    private val _notes = mutableStateListOf(
        Note(id = 1, title = "Note 1", content = "This is note one."),
        Note(id = 2, title = "Note 2", content = "This is note two."),
        Note(id = 3, title = "Note 3", content = "This is note four.") // Skipped 3? maybe just test data
    )

    val notes: SnapshotStateList<Note> = _notes

    fun addNote(note: Note) {
        _notes.add(note)
    }

    fun removeNote(note: Note) {
        _notes.remove(note)
    }

    fun updateNote(index: Int, updatedNote: Note) {
        _notes[index] = updatedNote
    }
}
