package com.example.notes_app_in_kotlin.tasks

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.adapter.TaskAdapter
import com.example.notes_app_in_kotlin.adapter.UsersAdapter
import com.example.notes_app_in_kotlin.databinding.ActivityTaskBinding
import com.example.notes_app_in_kotlin.helper.Global
import com.example.notes_app_in_kotlin.helper.NetworkUtilities
import com.example.notes_app_in_kotlin.register.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TaskActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTaskBinding
    private lateinit var  view : View
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    var list: ArrayList<Tasks> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        initializeFields()
        initObserver()
        setUpRecyclerViewOfTasks()
    }

    private fun setUpRecyclerViewOfTasks() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTask.isVisible = true
        binding.rvTask.layoutManager = layoutManager
        binding.rvTask.adapter = TaskAdapter()

    }

    private fun initObserver() {
        if(NetworkUtilities.getConnectivityStatus(this)) {

            getUserTask()

            val id = intent.getStringExtra("id")
            val randomKey = intent.getStringExtra("randomKey")

            //Retrieve data from Firebase Database
            if (id != null) {
                if (randomKey != null) {
                    database.child(id).child("tasks").child(randomKey).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                           list.clear()

                            for (dataSnapshot in snapshot.children) {
                                val tasks = dataSnapshot.getValue(Tasks::class.java) // Users Class
                                // users.setUserId(dataSnapshot.key)


                                    list.add(tasks!!)

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }


        }
        else{
            Global.showSnackBar(view,resources.getString(R.string.no_internet))
        }
    }

    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_task)
        view= binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
    }

    private fun getUserTask(){

        val id  = intent.getStringExtra("id")
        if (id != null) {
            database.child(id).child("name").addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (dataSnapshot in snapshot.children) {
                        val tasks = dataSnapshot.getValue(Tasks::class.java) // Users Class
                        // users.setUserId(dataSnapshot.key)
                        tasks?.id = dataSnapshot.key.toString()


                        list.add(tasks!!)

                    }
                    val adapter : TaskAdapter = TaskAdapter()
                     adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}