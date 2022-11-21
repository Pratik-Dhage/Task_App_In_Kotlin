package com.example.notes_app_in_kotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.notes_app_in_kotlin.databinding.ActivityMainBinding
import com.example.notes_app_in_kotlin.helper.Global
import com.example.notes_app_in_kotlin.helper.NetworkUtilities
import com.example.notes_app_in_kotlin.register.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//HOME Activity
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var  view : View
    private var isUserLoggedIn : Boolean = false
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    var list: ArrayList<Users> = ArrayList<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeFields()
        onClickListener()
        initObserver()
    }

    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        view= binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
    }

    private fun onClickListener() {
        TODO("Not yet implemented")
    }

    private fun initObserver() {

        if(NetworkUtilities.getConnectivityStatus(this)) {


              val fullName = binding.txtUserName.text.toString()
              val age = binding.txtUserAge.text.toString()
              val dob = binding.txtUserDateOfBirth.text.toString()


           }



        else{
            Global.showSnackBar(view,resources.getString(R.string.no_internet))
        }

}

}

