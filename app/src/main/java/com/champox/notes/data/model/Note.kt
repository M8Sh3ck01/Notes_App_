package com.champox.notes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val lastModified: Date = Date(),
    val isPinned: Boolean = false,
    val color: Int = 0
) {
    companion object {
        fun empty() = Note(title = "", content = "")
    }
}
