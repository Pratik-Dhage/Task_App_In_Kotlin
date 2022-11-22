package com.example.notes_app_in_kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.databinding.LvItemTaskBinding
import com.example.notes_app_in_kotlin.databinding.LvItemUsersBinding
import com.example.notes_app_in_kotlin.register.Users
import com.example.notes_app_in_kotlin.tasks.Tasks

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    var list: ArrayList<Tasks> =ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view : LvItemTaskBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lv_item_task,parent,false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val a = list.get(position)

        holder.binding.txtTask.text = a.task

    }

    override fun getItemCount(): Int =  if (list.isEmpty()) 0 else list.size

    class MyViewHolder(var binding: LvItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

}