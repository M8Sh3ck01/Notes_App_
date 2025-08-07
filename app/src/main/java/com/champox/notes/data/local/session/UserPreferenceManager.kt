package com.champox.notes.data.local.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "user_email"
        private const val KEY_LOGGED_IN = "is_logged_in"
        // Removed KEY_LOGIN_TIMESTAMP and SESSION_DURATION_MINUTES
    }

    fun saveUserSession(email: String) {
        sharedPref.edit {
            putString(KEY_EMAIL, email)
            putBoolean(KEY_LOGGED_IN, true)
            // Removed timestamp saving
        }
        UserSession.currentUserEmail = email
    }

    fun getCurrentUser(): String? {
        if (!isLoggedIn()) return null
        val email = sharedPref.getString(KEY_EMAIL, null)
        UserSession.currentUserEmail = email
        return email
    }

    fun isLoggedIn(): Boolean {
        // Simplified to just check the logged in flag
        return sharedPref.getBoolean(KEY_LOGGED_IN, false)
    }

    fun clearSession() {
        sharedPref.edit { clear() }
        UserSession.currentUserEmail = null
    }

    fun initializeSession() {
        if (isLoggedIn()) {
            UserSession.currentUserEmail = getCurrentUser()
        }
    }
}