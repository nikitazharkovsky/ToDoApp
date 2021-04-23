package com.example.todoapp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter()
    : RecyclerView.Adapter<ViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

    fun add(taskList: ArrayList<String>) {
        list.addAll(taskList)
    }

    fun clear() {
        list.clear()
    }
}
