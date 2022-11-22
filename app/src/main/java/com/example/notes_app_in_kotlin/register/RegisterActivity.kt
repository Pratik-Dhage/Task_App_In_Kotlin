package com.example.notes_app_in_kotlin.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.notes_app_in_kotlin.MainActivity
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.databinding.ActivityRegisterBinding
import com.example.notes_app_in_kotlin.helper.Global
import com.example.notes_app_in_kotlin.helper.NetworkUtilities
import com.example.notes_app_in_kotlin.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var view : View
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeFields()
        onClickListener()
    }


    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        view= binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
    }

    private fun onClickListener() {
        binding.txtSignIn.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
        }

        binding.btnSignUp.setOnClickListener {
            if(validations()) {

                  if(NetworkUtilities.getConnectivityStatus(this)){

                      Global.showSnackBar(view,resources.getString(R.string.Loading))
                  }

                  else{
                      Global.showSnackBar(view,resources.getString(R.string.no_internet))
                  }
            }


            }
        }



    private fun validations() : Boolean{

        val fullName =  binding.edtFullName.text.toString().trim()
        val email =  binding.edtEmail.text.toString().trim()
        val password =  binding.edtPass.text.toString().trim()
        val age =  binding.edtAge.text.toString().trim()
        val dob = binding.edtDob.text.toString().trim()

        if(fullName.isEmpty()) {
            Global.showSnackBar(view,resources.getString(R.string.fullname_error))
            return false
        }

        if (email.isEmpty() ){
            Global.showSnackBar(view,resources.getString(R.string.email_empty))
            return false
        }

        if (!Global.isValidEmail(email) ){
            Global.showSnackBar(view,resources.getString(R.string.email_error))
            return false
        }

        if (password.isEmpty()){
            Global.showSnackBar(view,resources.getString(R.string.password_empty))
            return false
        }

        if (password.length<6){
            Global.showSnackBar(view,resources.getString(R.string.password_error))
            return false
        }

        if (age.isEmpty() || age=="0"){
            Global.showSnackBar(view,resources.getString(R.string.age_error))
            return false
        }

        if(dob.isEmpty()){
            Global.showSnackBar(view,resources.getString(R.string.dob_error))
            return false
        }

        else {

            if(NetworkUtilities.getConnectivityStatus(this)){

                // create User in Firebase
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    if(it.isSuccessful && it.isComplete){

                       val users : Users = Users(fullName,email,password,age,dob)

                       val id: String = it.result.user?.uid ?: "" //currentUserKey for passing in MainActivity
                        // save data in RealTime Database
                        database.child(id).setValue(users)

                      val  currentUserKey = id // pass in MainActivity

                        Global.showToast(this,resources.getString(R.string.user_added_successfully))

                        val i = Intent(this, MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        i.putExtra("fullName",fullName)
                        i.putExtra("age",age)
                        i.putExtra("dob",dob)
                        i.putExtra("currentUserKey",currentUserKey)

                        startActivity(i)

                    }
                    else{
                        Global.showSnackBar(view, it.exception?.localizedMessage?.toString() ?: "")
                        println(("Here:" + it.exception?.localizedMessage?.toString()) ?: "")
                    }
                }

            }
            else{
                Global.showSnackBar(view,resources.getString(R.string.no_internet))
            }

            return true }
    }

    override fun onBackPressed() {
        // super.onBackPressed() // onBackPressed button disabled for Register Activity
    }


}
