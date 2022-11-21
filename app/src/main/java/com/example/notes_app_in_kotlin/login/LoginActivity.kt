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
import com.example.notes_app_in_kotlin.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var  view : View
    private var isUserLoggedIn : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeFields()
        onClickListener()
    }

    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        view= binding.root
    }

    private fun onClickListener() {

        binding.txtSignUp.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }

        binding.btnSignIn.setOnClickListener {
            if( validations() ) {

                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                else{
                    Global.showSnackBar(view,resources.getString(R.string.no_internet))
                }
            }

        }

    private fun validations():Boolean {

        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPass.text.toString().trim()


        if (email.isEmpty()  ){
            Global.showSnackBar(view,resources.getString(R.string.email_error))
            return false
        }

        if (password.isEmpty()){
            Global.showSnackBar(view,resources.getString(R.string.password_error))
            return false
        }

        else


        return true

    }

    override fun onBackPressed() {
        //  super.onBackPressed()
    }

}

