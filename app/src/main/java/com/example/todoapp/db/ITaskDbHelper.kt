package com.example.todoapp.db

interface ITaskDbHelper {
    fun add(task: String)
    fun update(oldTask: String, newTask: String)
    fun delete(task: String)
    fun loadPage(offset: Int, limit: Int): ArrayList<String>
}