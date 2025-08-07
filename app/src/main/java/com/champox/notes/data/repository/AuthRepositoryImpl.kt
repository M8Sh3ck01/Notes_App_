package com.champox.notes.data.repository

import com.champox.notes.data.local.dao.UserDao
import com.champox.notes.data.model.User

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun hasAnyAccounts(): Boolean {
        return userDao.getUserCount() > 0
    }

    override suspend fun register(email: String, password: String): Boolean {
        return if (userDao.getUser(email) == null) {
            userDao.insert(
                User(
                    email = email,
                    passwordHash = User.hashPassword(password)
                )
            )
            true
        } else false
    }

    override suspend fun login(email: String, password: String): Boolean {
        val user = userDao.getUser(email)
        return user?.passwordHash == User.hashPassword(password)
    }

    override suspend fun isEmailTaken(email: String): Boolean {
        return userDao.getUser(email) != null
    }
}