package com.example.notes_app_in_kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.databinding.LvItemUsersBinding
import com.example.notes_app_in_kotlin.register.Users
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

         holder.binding.txtName.text = a.name
         holder.binding.txtAge.text = a.age
         holder.binding.txtDateOfBirth.text = a.dob

    }

    override fun getItemCount(): Int =  if (list.isEmpty()) 0 else list.size

    class MyViewHolder(var binding: LvItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

}