package com.example.notes_app_in_kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes_app_in_kotlin.adapter.UsersAdapter
import com.example.notes_app_in_kotlin.databinding.ActivityMainBinding
import com.example.notes_app_in_kotlin.helper.Global
import com.example.notes_app_in_kotlin.helper.NetworkUtilities
import com.example.notes_app_in_kotlin.helper.SharedPreferencesHelper
import com.example.notes_app_in_kotlin.login.LoginActivity
import com.example.notes_app_in_kotlin.register.Users
import com.example.notes_app_in_kotlin.tasks.TaskActivity
import com.example.notes_app_in_kotlin.tasks.WriteTaskActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//HOME Activity
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var view: View
    private var isNightModeOn: Boolean = false
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var list: ArrayList<Users> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeFields()
        onClickListener()
        initObserver()
        setUpRecyclerViewOfUsers()
    }

    private fun onClickListener() {

        binding.txtSignOut.setOnClickListener {
            SharedPreferencesHelper.clearSharedPreferences()
            Global.removeStringInSharedPref(this,"id")
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)

            Global.showToast(this,resources.getString(R.string.signout_success))
        }

        //SwitcherView
        binding.switcherView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }

        //Floating Action Button
        binding.floatingButton.setOnClickListener {

            val id = intent.getStringExtra("id")
            val currentUserKey = intent.getStringExtra("currentUserKey")
            if(NetworkUtilities.getConnectivityStatus(this)){


                //from login
                if (id != null) {
                    val i = Intent(this,WriteTaskActivity::class.java)
                    i.putExtra("id",id)
                    startActivity(i)
                }

                val j = Intent(this,WriteTaskActivity::class.java)
                startActivity(j)

            }
            else{
                Global.showSnackBar(view, resources.getString(R.string.no_internet))
            }
        }

        //CurrentUser(Green) click
        binding.clCurrentUser.setOnClickListener {

            val id = intent.getStringExtra("id")
            val j = Intent(this,TaskActivity::class.java)
            j.putExtra("id",id)
            startActivity(j)

            //coming from Write Task Activity
            val randomKey = intent.getStringExtra("randomKey")
            if(NetworkUtilities.getConnectivityStatus(this)){

                if (randomKey != null) {
                    val i = Intent(this,TaskActivity::class.java)
                    i.putExtra("randomKey",randomKey)
                    startActivity(i)
                }
            }
            else{
                Global.showSnackBar(view, resources.getString(R.string.no_internet))
            }
        }
    }


    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        view = binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        isNightModeOn = AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_NO
    }



    private fun setUpRecyclerViewOfUsers() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMain.isVisible = true
        binding.rvMain.layoutManager = layoutManager
        binding.rvMain.adapter = UsersAdapter()

    }

    private fun initObserver() {

        if (NetworkUtilities.getConnectivityStatus(this)) {

            //coming from Register
            //get current user details
            if (intent.hasExtra("fullName")) {
                val fullName = intent.getStringExtra("fullName")
                binding.txtUserName.text = fullName
            }

            if (intent.hasExtra("age")) {
                val age = intent.getStringExtra("age")
                binding.txtUserAge.text = age
            }

            if (intent.hasExtra("dob")) {
                val dob = intent.getStringExtra("dob")
                binding.txtUserDateOfBirth.text = dob
            }

            //coming from Login
            if (intent.hasExtra("id")) {

                val id = intent.getStringExtra("id")
                if (id != null) {

                    //Retrieve data from Firebase Database
                    database.child(id).get().addOnSuccessListener {

                        if (it.exists()) {
                            val fullName = it.child("name").value
                            binding.txtUserName.text = fullName.toString()

                            val age = it.child("age").value
                            binding.txtUserAge.text = age.toString()

                            val dob = it.child("dob").value
                            binding.txtUserDateOfBirth.text = dob.toString()

                            //save user data from Login in SharedPreferences
                            Global.saveStringInSharedPref(this,"fullName",fullName.toString())
                            Global.saveStringInSharedPref(this,"age",age.toString())
                            Global.saveStringInSharedPref(this,"dob",dob.toString())

                        }
                    }.addOnFailureListener {
                        Global.showSnackBar(view, resources.getString(R.string.user_not_exist))
                    }


                }


            }

            //Other Users
              val currentUserKey = intent.getStringExtra("currentUserKey")
            val otherUsers = database.child(currentUserKey?:"")

            //Add Value Event Listener will load everytime when data changes
            //Add Listener For Single value will load once
          /*  otherUsers.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear() //acts like Refresh

                    for (dataSnapshot in snapshot.children) {
                        val users = dataSnapshot.getValue(Users::class.java) // Users Class
                        // users.setUserId(dataSnapshot.key)
                        users?.id = dataSnapshot.key.toString()

                        // this condition will not Allow Current Logged In User to Show in MainActivity's RecyclerView
                        // Rest all other Users using this App will be Displayed
                        if (!users?.id.equals(FirebaseAuth.getInstance().uid)) {
                            list.add(users!!)
                        }
                    }

                    val usersAdapter: UsersAdapter = UsersAdapter()
                    usersAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    Global.showSnackBar(view, error.message.toString())
                }

            })*/



        } else {
            Global.showSnackBar(view, resources.getString(R.string.no_internet))
        }

        getAllUserData() // to get all user data


    }

    override fun onBackPressed() {
        // super.onBackPressed() // onBackPressed button disabled
    }



    private fun getAppInstalledUsers(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)

            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    private fun getAllUserData(){

        binding.txtUserName.text = Global.getStringFromSharedPref(this,"fullName")
        binding.txtUserAge.text = Global.getStringFromSharedPref(this,"age")
        binding.txtUserDateOfBirth.text = Global.getStringFromSharedPref(this,"dob")

    }




}

