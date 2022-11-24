package com.example.notes_app_in_kotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.databinding.LvItemUsersBinding
import com.example.notes_app_in_kotlin.helper.Global
import com.example.notes_app_in_kotlin.register.Users
import com.example.notes_app_in_kotlin.tasks.TaskActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.reflect.Array

class UsersAdapter(var list: ArrayList<Users>) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.MyViewHolder {

        val view : LvItemUsersBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lv_item_users,parent,false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdapter.MyViewHolder, position: Int) {

        val a = list.get(position)
        val context = holder.itemView.context

         holder.binding.txtName.text = a.name
         holder.binding.txtAge.text = a.age
         holder.binding.txtDateOfBirth.text = a.dob

        //on clicking single user , display that user task
        holder.itemView.setOnClickListener {

            var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
            database.get().addOnSuccessListener {

                val idKey = it.key.toString() //Users Node


                Global.showToast(context,idKey) //Users Node

                val i = Intent(context, TaskActivity::class.java)
                i.putExtra("idKey",idKey)

                context.startActivity(i)

                database.get().addOnSuccessListener {

                        Global.showToast(context,it.childrenCount.toString())




                }


            }

        }


    }

    override fun getItemCount(): Int =  if (list.isEmpty()) 0 else list.size

    class MyViewHolder(var binding: LvItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

}