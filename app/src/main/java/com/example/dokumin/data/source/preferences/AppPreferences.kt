package com.example.dokumin.data.source.preferences

import android.content.Context


class AppPreferences(val mContext: Context) {
    companion object {
        const val prefName = "usr_token"
        const val tokenKey = "usr_token"
        const val isFirst = true
    }

    private val preferences = mContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun deleteSession() {
        editor.apply {
            clear()
            apply()
            commit()
        }
    }

    fun saveSession(token: String) {
        editor.apply {
            putString(tokenKey, token)
            apply()
            commit()
        }
    }

    fun isFirstRun(): Boolean {
        return preferences.getBoolean(isFirst.toString(), true)
    }

    fun updateFirstRun() {
        editor.apply {
            putBoolean(isFirst.toString(), false)
            apply()
            commit()
        }
    }


    fun getSession(): String {
        val token = preferences.getString(tokenKey, "")
        return token.toString()
    }
}