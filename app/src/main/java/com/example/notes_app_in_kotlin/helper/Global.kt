package com.example.notes_app_in_kotlin.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.notes_app_in_kotlin.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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


    @SuppressLint("InflateParams")
    fun showDatePicker(activity: Activity, commonInterface: OnClickDate, type: String) {
        val parentView =
            activity.layoutInflater.inflate(R.layout.layout_bottom_datepicker_dialog, null)
        val bottomSheetDialog = BottomSheetDialog(activity)
        bottomSheetDialog.setContentView(parentView)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(false)
        val datePicker = parentView.findViewById(R.id.datePicker) as DatePicker
        val txtDone = parentView.findViewById(R.id.txtDone) as TextView
        val txtCancel = parentView.findViewById(R.id.txtCancel) as TextView


        when (type) {
            "DOB" -> {
                val strDob = getStringFromSharedPref(activity,"dob")
                if (strDob.isNotEmpty()) {
                    val strDobArray = strDob.split("-")
                    if (strDobArray.size > 2 && strDobArray[0].isNotEmpty() && strDobArray[1].isNotEmpty() && strDobArray[2].isNotEmpty()) {
                        datePicker.updateDate(
                            strDobArray[0].toInt(),
                            strDobArray[1].toInt() - 1,
                            strDobArray[2].toInt()
                        )
                    }
                }
                val c = Calendar.getInstance()
                datePicker.maxDate = c.timeInMillis
            }

        }


        txtCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        txtDone.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = Calendar.getInstance()
            date.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            commonInterface.onClick(simpleDateFormat.format(date.time))
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    fun getFormattedDate(inputFormat: String, outputFormat: String, value: String): String {
        var newDateStr = ""
        if (value.isNotEmpty()) {
            val inputFormatter = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            val dateObj: Date
            try {
                dateObj = inputFormatter.parse(value)!!
                val outputFormatter = SimpleDateFormat(outputFormat, Locale.ENGLISH)
                newDateStr = outputFormatter.format(dateObj)
            } catch (e: ParseException) {
                newDateStr = value
            }
        }
        return newDateStr
    }


}