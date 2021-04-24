package com.example.todoapp

import android.widget.ProgressBar
import android.widget.TextView
import com.example.todoapp.db.ITaskDbHelper
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
public class DialogActionsShould {

    @Mock
    lateinit var progressBar: ProgressBar

    @Mock
    lateinit var dbHelper: ITaskDbHelper

    @Mock
    lateinit var mainActivity: IMainActivity

    @Mock
    lateinit var exceptionTextView: TextView

    @Test
    fun saveNewTask() {
        val expectedTask = "ToDo task"

        val sut = DialogActions(progressBar, dbHelper, mainActivity)
        val actual = sut.saveTask(exceptionTextView, expectedTask)

        verify(dbHelper, times(1)).add(expectedTask)
        verify(mainActivity, times(1)).updateUI()
        verify(exceptionTextView, times(1)).setText("")
        assertTrue(actual)
    }

    @Test
    fun returnFalseIfAnyExceptionOnAdd() {
        val task = "ToDo task"
        val expectedError = "Some error"

        `when`(dbHelper.add(ArgumentMatchers.anyString())).thenThrow(RuntimeException(expectedError))

        val sut = DialogActions(progressBar, dbHelper, mainActivity)
        val actual = sut.saveTask(exceptionTextView, task)

        verify(dbHelper, times(1)).add(task)
        verify(exceptionTextView, times(1)).setText(expectedError)

        assertFalse(actual)
    }

    @Test
    fun editTask() {
        val oldTask = "ToDo task old"
        val newTask = "ToDo task new"

        val sut = DialogActions(progressBar, dbHelper, mainActivity)
        val actual = sut.editTask(exceptionTextView, oldTask, newTask)

        verify(dbHelper, times(1)).update(oldTask, newTask)
        verify(mainActivity, times(1)).updateUI()
        verify(exceptionTextView, times(1)).setText("")
        assertTrue(actual)
    }

    @Test
    fun returnFalseIfAnyExceptionOnUpdate() {
        val oldTask = "ToDo task old"
        val newTask = "ToDo task new"
        val expectedError = "Some error"

        `when`(dbHelper.update(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenThrow(RuntimeException(expectedError))

        val sut = DialogActions(progressBar, dbHelper, mainActivity)
        val actual = sut.editTask(exceptionTextView, oldTask, newTask)

        verify(dbHelper, times(1)).update(oldTask, newTask)
        verify(exceptionTextView, times(1)).setText(expectedError)

        assertFalse(actual)
    }

    @Test
    fun deleteTask() {
        val task = "My Task"

        val sut = DialogActions(progressBar, dbHelper, mainActivity)
        val actual = sut.deleteTask(task)

        verify(dbHelper, times(1)).delete(task)
        verify(mainActivity, times(1)).updateUI()
        assertTrue(actual)
    }

    @Test
    fun returnFalseIfAnyExceptionOnDelete() {
        val task = "My Task"

        `when`(dbHelper.delete(ArgumentMatchers.anyString())).thenThrow(RuntimeException())

        val sut = DialogActions(progressBar, dbHelper, mainActivity)
        val actual = sut.deleteTask(task)

        verify(dbHelper, times(1)).delete(task)
        assertFalse(actual)
    }
}