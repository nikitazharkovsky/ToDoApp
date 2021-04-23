package com.example.todoapp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R

class ViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_todo, parent, false)) {

    private var taskView: TextView? = null

    init {
        taskView = itemView.findViewById(R.id.task_title)
    }

    fun bind(task: String) {
        taskView?.text = task
    }
}