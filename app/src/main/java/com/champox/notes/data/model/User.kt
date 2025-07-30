package com.champox.notes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.MessageDigest

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,  // Email as primary key
    val passwordHash: String,      // Store only hashed passwords
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        // Simple SHA-256 hashing (for learning purposes)
        fun hashPassword(password: String): String {
            val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}