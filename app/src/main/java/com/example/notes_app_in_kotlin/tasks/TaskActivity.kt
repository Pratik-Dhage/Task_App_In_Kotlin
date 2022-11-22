package com.example.notes_app_in_kotlin.tasks

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

            val id = intent.getStringExtra("id")
            val randomKey = intent.getStringExtra("randomKey")

            //Retrieve data from Firebase Database
            if (id != null) {
                if (randomKey != null) {
                    database.child(id).child("tasks").child(randomKey).get().addOnSuccessListener {
                        if(it.exists()){

                            val t = it.child("task")

                        }
                    }
                }
            }


        }
        else{
            Global.showSnackBar(view,resources.getString(R.string.no_internet))
        }
    }

    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        view= binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
    }
}