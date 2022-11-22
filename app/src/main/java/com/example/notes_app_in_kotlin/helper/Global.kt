package com.example.notes_app_in_kotlin.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.Layout
import android.text.TextUtils
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.snackbar.Snackbar



object Global {

    fun showToast(context: Context, str:String){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(view: View, str : String){
        val snackBar : Snackbar = Snackbar.make(view,str,Snackbar.LENGTH_SHORT)
        snackBar.show()
    }

    /*fun isValidEmail(target: String): Boolean =
        !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()*/

     fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun hideKeyboard(view : View) {
        val inputManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun saveStringInSharedPref(context: Context, key: String, value: String) {
        SharedPreferencesHelper.writeString(context, key, value)
    }

    fun getStringFromSharedPref(context: Context, key: String): String {
        return SharedPreferencesHelper.getString(context, key, "") ?: ""
    }

    fun removeStringInSharedPref(context: Context, key: String) {
        SharedPreferencesHelper.writeString(context, key, "")
    }

}