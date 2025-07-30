package com.champox.notes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.champox.notes.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY lastModified DESC")
    fun getNotesForUser(userId: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isPinned = 1 AND userId = :userId ORDER BY lastModified DESC")
    fun getPinnedNotes(userId: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isArchived = 1 AND userId = :userId ORDER BY lastModified DESC")
    fun getArchivedNotes(userId: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isFavorite = 1 AND userId = :userId ORDER BY lastModified DESC")
    fun getFavoriteNotes(userId: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?
}
