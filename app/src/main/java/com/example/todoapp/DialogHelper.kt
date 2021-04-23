package com.example.todoapp

import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import com.example.todoapp.db.ITaskDbHelper
import java.lang.Exception
import java.util.*
import kotlin.concurrent.timerTask

class DialogHelper(
        private val progressBar: ProgressBar,
        private val dbHelper: ITaskDbHelper,
        private val mainActivity: IMainActivity
) {
    private val stopProgressBar = Runnable { progressBar.visibility = ProgressBar.GONE }

    fun saveDialogAction(dialog: AlertDialog, taskEditText: EditText, exceptionTextView: TextView) {
        val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.isEnabled = false
            taskEditText.doAfterTextChanged {
            button.isEnabled = taskEditText.text.isNotEmpty()
        }

        button.setOnClickListener {
            val newTask = taskEditText.text.toString()
            progressBar.visibility = ProgressBar.VISIBLE
            Timer().schedule(timerTask {
                try {
                    exceptionTextView.text = ""

                    dbHelper.add(newTask)
                    mainActivity.updateUI()
                    mainActivity.runOnUiThread(stopProgressBar)

                    //Dismiss once everything is OK.
                    dialog.dismiss()
                } catch (e: Exception) {
                    exceptionTextView.text = e.message
                }
            }, 0)
        }
    }

    fun editDialogAction(dialog: AlertDialog, taskEditText: EditText, exceptionTextView: TextView, originalTask: String) {
        val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            val newTask = taskEditText.text.toString()
            progressBar.visibility = ProgressBar.VISIBLE
            Timer().schedule(timerTask {
                try {
                    exceptionTextView.text = ""

                    dbHelper.update(originalTask, newTask)
                    mainActivity.updateUI()
                    mainActivity.runOnUiThread(stopProgressBar)

                    //Dismiss once everything is OK.
                    dialog.dismiss()
                } catch (e: Exception) {
                    exceptionTextView.text = e.message
                }
            }, 0)
        }
    }

    fun deleteDialogAction(dialog: AlertDialog, task: String) {
        val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            progressBar.visibility = ProgressBar.VISIBLE
            Timer().schedule(timerTask {
                dbHelper.delete(task)
                mainActivity.updateUI()
                mainActivity.runOnUiThread(stopProgressBar)
            }, 0)
        }
    }
}