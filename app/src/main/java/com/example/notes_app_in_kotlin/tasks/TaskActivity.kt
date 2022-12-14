package com.example.notes_app_in_kotlin.tasks

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app_in_kotlin.MainActivity
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
     private lateinit var list: ArrayList<Tasks>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        initializeFields()
        setUpRecyclerViewOfTasks()
     //   initObserver()

    }

    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_task)
        view= binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
    }

    private fun setUpRecyclerViewOfTasks() {

        val recyclerView : RecyclerView = binding.rvTask
        list = arrayListOf<Tasks>()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTask.isVisible = true
        binding.rvTask.layoutManager = layoutManager


        //fetch data from firebase
        val id = Global.getStringFromSharedPref(this,"id")
        val randomKey = intent.getStringExtra("randomKey")
       val otherId = intent.getStringExtra("otherId")


        // if clicking on current user
        if(otherId==null){

            if (randomKey != null) {
                database.child(id).child("tasks").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){

                            for(idd in snapshot.children){

                                val d = idd.getValue(Tasks::class.java)
                                list.add(d!!)
                            }
                            binding.rvTask.adapter = TaskAdapter(list)
                            recyclerView.adapter = TaskAdapter(list)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }

            else{

                database.child(id).child("tasks").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){

                            for(idd in snapshot.children){

                                val d = idd.getValue(Tasks::class.java)
                                list.add(d!!)
                            }
                            binding.rvTask.adapter = TaskAdapter(list)
                            recyclerView.adapter = TaskAdapter(list)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })
            }


        }

        //if clicking on other users
       if(otherId!=null)
        {

            otherUsers()

        }


    }

    // for Other Users Individual Tasks

    fun otherUsers(){

        // for Other Users Individual Tasks

        val otherId = intent.getStringExtra("otherId")

        if (otherId != null) {
            database.child(otherId).child("tasks").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){

                        for(idd in snapshot.children){


                            val d = idd.getValue(Tasks::class.java)

                            //condition to not include self user
                            if(otherId!=FirebaseAuth.getInstance().uid){
                                list.add(d!!)
                            }

                        }
                        binding.rvTask.adapter = TaskAdapter(list)
                        val recyclerView : RecyclerView = binding.rvTask
                        recyclerView.adapter = TaskAdapter(list)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        }

    }






    override fun onBackPressed() {
        super.onBackPressed()

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}