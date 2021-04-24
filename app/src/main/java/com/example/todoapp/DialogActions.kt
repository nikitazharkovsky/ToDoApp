package com.example.todoapp

import android.widget.ProgressBar
import android.widget.TextView
import com.example.todoapp.db.ITaskDbHelper
import java.lang.Exception

interface IDialogActions {
    fun prepareForAction()
    fun saveTask(
        exceptionTextView: TextView,
        newTask: String
    ): Boolean

    fun editTask(
        exceptionTextView: TextView,
        originalTask: String,
        newTask: String
    ): Boolean

    fun deleteTask(task: String): Boolean
}

class DialogActions (
    private val progressBar: ProgressBar,
    private val dbHelper: ITaskDbHelper,
    private val mainActivity: IMainActivity
) : IDialogActions {
    private val stopProgressBar = Runnable { progressBar.visibility = ProgressBar.GONE }

    override fun prepareForAction() {
        progressBar.visibility = ProgressBar.VISIBLE
    }

    override fun saveTask(
        exceptionTextView: TextView,
        newTask: String
    ): Boolean {
        return try {
            exceptionTextView.text = ""
            dbHelper.add(newTask)
            mainActivity.updateUI()
            mainActivity.runOnUiThread(stopProgressBar)
            true
        } catch (e: Exception) {
            exceptionTextView.text = e.message
            false
        }
    }

    override fun editTask(
        exceptionTextView: TextView,
        originalTask: String,
        newTask: String
    ): Boolean {
        return try {
            exceptionTextView.text = ""
            dbHelper.update(originalTask, newTask)
            mainActivity.updateUI()
            mainActivity.runOnUiThread(stopProgressBar)
            true
        } catch (e: Exception) {
            exceptionTextView.text = e.message
            false
        }
    }

    override fun deleteTask(task: String): Boolean {
        return try {
            dbHelper.delete(task)
            mainActivity.updateUI()
            mainActivity.runOnUiThread(stopProgressBar)

            true
        } catch (e: Exception) {
            //ignore
            false
        }
    }
}