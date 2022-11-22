package com.example.notes_app_in_kotlin.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.notes_app_in_kotlin.MainActivity
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.databinding.ActivityLoginBinding
import com.example.notes_app_in_kotlin.helper.Global
import com.example.notes_app_in_kotlin.helper.NetworkUtilities
import com.example.notes_app_in_kotlin.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var  view : View
    private var isUserLoggedIn : Boolean = false
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeFields()
        onClickListener()
    }

    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        view= binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
    }

    private fun onClickListener() {

        binding.txtSignUp.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }

        binding.btnSignIn.setOnClickListener {
            if( validations() ) {

                     if(NetworkUtilities.getConnectivityStatus(this)){

                         Global.showSnackBar(view,resources.getString(R.string.login_successful))
                     }
                     else{
                         Global.showSnackBar(view,resources.getString(R.string.no_internet))
                     }

                }

            }

        }

    private fun validations():Boolean {

        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPass.text.toString().trim()

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

        else
        {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

        return true

    }

    override fun onBackPressed() {
        //  super.onBackPressed()
    }

}

