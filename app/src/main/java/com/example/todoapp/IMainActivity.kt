package com.example.todoapp

interface IMainActivity {
    fun updateUI()
    fun runOnUiThread(action: Runnable)
}