package com.example.connecturpet.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.connecturpet.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Function to delete auth token
     */
    fun deleteAuthToken() {
        val editor = prefs.edit()
        editor.remove (USER_TOKEN)
        editor.apply()
    }
}