package com.example.todoapp

import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.todoapp.db.ITaskDbHelper
import com.example.todoapp.db.TaskDbHelper
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
public class DialogHelperTest {

    @Mock
    lateinit var progressBar: ProgressBar

    @Mock
    lateinit var dbHelper: ITaskDbHelper

    @Mock
    lateinit var mainActivity: IMainActivity

    @Test
    fun saveAction_positiveButtonNotEnabledOnStart() {
        var actual = true
        val dialog: AlertDialog = mock(AlertDialog::class.java)
        val editText: EditText = mock(EditText::class.java)
        val textView: TextView = mock(TextView::class.java)
        val positiveButton: Button = mock(Button::class.java)

        `when`(dialog.getButton(AlertDialog.BUTTON_POSITIVE)).thenReturn(positiveButton)

        val sut = DialogHelper(progressBar, dbHelper, mainActivity)
        sut.saveDialogAction(dialog, editText, textView)

        verify(positiveButton, times(1)).isEnabled = false;
    }

//    @Test
//    fun saveAction_positiveButtonEnabledAfterText() {
//        var actual = true
//        val dialog: AlertDialog = mock(AlertDialog::class.java)
//        val editText: EditText = mock(EditText::class.java)
//        val textView: TextView = mock(TextView::class.java)
//        val positiveButton: Button = mock(Button::class.java)
//
//        `when`(dialog.getButton(AlertDialog.BUTTON_POSITIVE)).thenReturn(positiveButton)
//        `when`(positiveButton.isEnabled).thenReturn(actual)
//
//        val sut = DialogHelper(progressBar, dbHelper, mainActivity)
//        sut.saveDialogAction(dialog, editText, textView)
//
//        verify(positiveButton, times(1)).isEnabled = false;
//        verify(positiveButton, never()).isEnabled = true;
//
//        editText.setText("new text")
//
//        verify(positiveButton, times(2)).isEnabled = true;
//    }
}