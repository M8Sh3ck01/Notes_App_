package com.champox.notes.data

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis() // default to now

)
