package com.example.notes_app_in_kotlin.tasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.notes_app_in_kotlin.MainActivity
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.databinding.ActivityTaskBinding
import com.example.notes_app_in_kotlin.databinding.ActivityWriteTaskBinding
import com.example.notes_app_in_kotlin.helper.Global
import com.example.notes_app_in_kotlin.helper.NetworkUtilities
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WriteTaskActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWriteTaskBinding
    private lateinit var  view : View
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initializeFields()
        onClickListener()
    }

    private fun onClickListener() {

       binding.btnTaskSubmit.setOnClickListener {

           val id = intent.getStringExtra("id")
           val currentTask =binding.edtTask.text.toString()

           if(NetworkUtilities.getConnectivityStatus(this)){

               if(id!=null){
                   //save task in firebase database
                   val randomKey= database.child(id).child("tasks").push().key // push will create new node with unique key

                    val task = Tasks(currentTask,randomKey?:"")

                   if (randomKey != null) {
                       database.child(id).child("tasks").child(randomKey).setValue(task)

                       Global.showToast(this,resources.getString(R.string.task_added_successfully))
                       Global.hideKeyboard(view)
                       binding.edtTask.text.clear()

                       //pass randomKey  to TaskActivity
                       val i = Intent(this, TaskActivity::class.java)
                       i.putExtra("randomKey",randomKey)
                       startActivity(i)
                   }
               }
               else{
                   //if id is null from login get id from SharedPref
                   val id = Global.getStringFromSharedPref(this,"id")

                   //save task in firebase database
                   val randomKey= database.child(id).child("tasks").push().key // push will create new node with unique key

                   val task = Tasks(currentTask,randomKey?:"")

                   if (randomKey != null) {
                       database.child(id).child("tasks").child(randomKey).setValue(task)

                       Global.showToast(this,resources.getString(R.string.task_added_successfully))
                       Global.hideKeyboard(view)
                       binding.edtTask.text.clear()

                      /* //pass randomKey to MainActivity and then from MainActivity to TaskActivity
                       val i = Intent(this, MainActivity::class.java)
                       i.putExtra("randomKey",randomKey)
                       startActivity(i)*/

                       //pass randomKey  to TaskActivity
                       val i = Intent(this, TaskActivity::class.java)
                       i.putExtra("randomKey",randomKey)
                       startActivity(i)
                   }
               }


                 }
           else{
               Global.showSnackBar(view,resources.getString(R.string.no_internet))
           }

       }
    }

    private fun initializeFields() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_task)
        view = binding.root
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }
}