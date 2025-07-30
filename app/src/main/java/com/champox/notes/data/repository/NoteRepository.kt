package com.champox.notes.data.repository

import com.champox.notes.data.local.dao.NoteDao
import com.champox.notes.data.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getUserNotes(userId: String): Flow<List<Note>> {
        return noteDao.getNotesForUser(userId)
    }

    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    fun getPinnedNotes(userId: String): Flow<List<Note>> = noteDao.getPinnedNotes(userId)

    fun getArchivedNotes(userId: String): Flow<List<Note>> = noteDao.getArchivedNotes(userId)

    fun getFavoriteNotes(userId: String): Flow<List<Note>> = noteDao.getFavoriteNotes(userId)

    suspend fun insertNote(note: Note): Long {
        return noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.delete(note)
    }

    suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)
    }
}
