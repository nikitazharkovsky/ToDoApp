package com.example.todoapp

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.internal.verification.VerificationModeFactory.times
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class DialogHelperShould {

    @Mock
    lateinit var dialogActions: IDialogActions

    @Mock
    lateinit var dialog: AlertDialog

    @Mock
    lateinit var taskEditText: EditText

    @Mock
    lateinit var exceptionTextView: TextView

    @Mock
    lateinit var positiveButton: Button

    @Test
    fun positiveButton_NotEnabled_onSave() {
        `when`(dialog.getButton(AlertDialog.BUTTON_POSITIVE)).thenReturn(positiveButton)

        val sut = DialogHelper(dialogActions)
        sut.saveDialogAction(dialog, taskEditText, exceptionTextView)

        verify(positiveButton, times(1)).setEnabled(false)
    }

}