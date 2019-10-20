package com.mm.montymobile.helper

import android.content.Context
import android.content.SharedPreferences


class AppPreferences(context: Context) {

    private val authPreferences: SharedPreferences


    init {
        this.authPreferences = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    }

    fun getStringAuth(key: String): String? {
        return authPreferences.getString(key, "")
    }

    fun putStringAuth(key: String, value: String) {
        val editor = authPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun getBooleanAuth(key: String): Boolean? {
        return authPreferences.getBoolean(key, false)
    }

    fun putBooleanAuth(key: String, value: Boolean) {
        val editor = authPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun clearAuthPreferences() {
        val editor = authPreferences.edit()
        editor.clear()
        editor.apply()
    }



    companion object {
        val APP_PREFERENCES_FILE_NAME = "user"
        val NUMBER = "number"
        val ISCONTACTEDSAVE = "isContactSaved"


    }
}