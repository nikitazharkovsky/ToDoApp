package com.example.todoapp

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import java.util.*
import kotlin.concurrent.timerTask

class DialogHelper(
    private val dialogActions: IDialogActions
) {

    fun saveDialogAction(dialog: AlertDialog, taskEditText: EditText, exceptionTextView: TextView) {
        val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        //button.isEnabled = false
        taskEditText.doAfterTextChanged {
            button.isEnabled = taskEditText.text.isNotEmpty()
        }

        button.setOnClickListener {
            val newTask = taskEditText.text.toString()
            dialogActions.prepareForAction()
            Timer().schedule(timerTask {
                if (dialogActions.saveTask(exceptionTextView, newTask)) {
                    //Dismiss once everything is OK.
                    dialog.dismiss()
                }
            }, 0)
        }
    }

    fun editDialogAction(
        dialog: AlertDialog,
        taskEditText: EditText,
        exceptionTextView: TextView,
        originalTask: String
    ) {
        val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            val newTask = taskEditText.text.toString()
            dialogActions.prepareForAction()
            Timer().schedule(timerTask {
                if (dialogActions.editTask(exceptionTextView, originalTask, newTask)) {
                    //Dismiss once everything is OK.
                    dialog.dismiss()
                }
            }, 0)
        }
    }

    fun deleteDialogAction(dialog: AlertDialog, task: String) {
        val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            dialogActions.prepareForAction()
            Timer().schedule(timerTask {
                if (dialogActions.deleteTask(task)) {
                    //Dismiss once everything is OK.
                    dialog.dismiss()
                }
            }, 0)
        }
    }
}

