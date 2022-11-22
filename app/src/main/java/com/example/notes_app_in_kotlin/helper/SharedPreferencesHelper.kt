package com.example.notes_app_in_kotlin.helper

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {

    private const val SHARED_PREFS_NAME = "Notes_App_In_Kotlin"
    private var sharedPreferences: SharedPreferences? = null

    /**
     * Write string to shared preferences.
     *
     * @param context the context
     * @param key     the key
     * @param value   the value
     * @return true if value was written successfully
     */
    fun writeString(context: Context, key: String, value: String): Boolean {
        if (context == null) {
            return false
        }
        getSharedPreferencesEditor(context)?.putString(key, value)?.apply()
        return true
    }


    /**
     * Get string to shared preferences.
     *
     * @param context      the context
     * @param key          the key
     * @param defaultValue the default value
     * @return string for this key, or defaultValue if not found
     */
    fun getString(context: Context, key: String, defaultValue: String): String? {
        return if (context == null) {
            defaultValue
        } else getSharedPreferences(context)?.getString(key, defaultValue)
    }


    private fun getSharedPreferences(context: Context): SharedPreferences? {

        if (context == null) {
            return null
        }
        if (sharedPreferences == null) {


            run {
                sharedPreferences =
                    context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            }

        }
        return sharedPreferences
    }

    /**
     * Gets shared preferences editor.
     *
     * @param context the context
     * @return the shared preferences editor
     */
    private fun getSharedPreferencesEditor(context: Context): SharedPreferences.Editor? {

        return if (context == null) {
            null
        } else getSharedPreferences(context)?.edit()
    }

    fun clearSharedPreferences() {
        sharedPreferences?.edit()?.clear()?.commit()
    }

}