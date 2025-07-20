package com.champox.notes.data.repository

import com.champox.notes.data.local.dao.UserDao
import com.champox.notes.data.model.User

class AuthRepository(private val userDao: UserDao) {
    suspend fun signUp(username: String, password: String): Boolean {
        val existing = userDao.getUserByUsername(username)
        if (existing != null) return false
        userDao.insertUser(User(username = username, password = password))
        return true
    }

    suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return user?.password == password
    }
} 