package com.example.notes_app_in_kotlin

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.notes_app_in_kotlin.register.Users
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

        binding.switcherView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }

        binding.floatingButton.setOnClickListener {

            val id = intent.getStringExtra("id")
            if(NetworkUtilities.getConnectivityStatus(this)){

                if (id != null) {
                    val i = Intent(this,WriteTaskActivity::class.java)
                    i.putExtra("id",id)
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

                        }
                    }.addOnFailureListener {
                        Global.showSnackBar(view, resources.getString(R.string.user_not_exist))
                    }


                }


            }

            //Other Users

            val otherUsers = database.child("Users")

            //Add Value Event Listener will load everytime when data changes
            //Add Listener For Single value will load once
            otherUsers.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear() //acts like Refresh

                    for (dataSnapshot in snapshot.children) {
                        val users = dataSnapshot.getValue(Users::class.java) // Users Class
                        // users.setUserId(dataSnapshot.key)

                        // this condition will not Allow Current Logged In User to Show in MainActivity's RecyclerView
                        // Rest all other Users using this App will be Displayed
                        if (!users?.name.equals(FirebaseAuth.getInstance().currentUser?.displayName)) {
                            list.add(users!!)
                        }
                    }

                    val usersAdapter: UsersAdapter = UsersAdapter()
                    usersAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    Global.showSnackBar(view, error.message.toString())
                }

            })

        } else {
            Global.showSnackBar(view, resources.getString(R.string.no_internet))
        }

    }

    override fun onBackPressed() {
        // super.onBackPressed() // onBackPressed button disabled
    }


}

