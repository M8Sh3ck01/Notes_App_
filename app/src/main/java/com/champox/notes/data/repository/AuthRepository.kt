package com.champox.notes.data.repository

interface AuthRepository {
    suspend fun register(email: String, password: String): Boolean
    suspend fun login(email: String, password: String): Boolean
    suspend fun isEmailTaken(email: String): Boolean
    suspend fun hasAnyAccounts(): Boolean

}