package com.example.todoapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime

class TaskDbHelper(context: Context)
    : SQLiteOpenHelper(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION),
    ITaskDbHelper {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_LAST_UPDATE + " DATETIME NOT NULL);"

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE)
        onCreate(db)
    }

    override fun add(task: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task)
        values.put(TaskContract.TaskEntry.COL_LAST_UPDATE, LocalDateTime.now().toString())
        db.insertWithOnConflict(
                TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
    }

    override fun update(oldTask: String, newTask: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, newTask)
        values.put(TaskContract.TaskEntry.COL_LAST_UPDATE, LocalDateTime.now().toString())
        db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE + "=?", arrayOf(oldTask)).toLong()
        db.close()
    }

    override fun delete(task: String) {
        val db = writableDatabase
        db.delete(
                TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                arrayOf(task))
        db.close()
    }

    override fun loadPage(offset: Int, limit: Int): ArrayList<String> {
        val db = readableDatabase
        val taskList = ArrayList<String>()
        val cursor = db.query(
                TaskContract.TaskEntry.TABLE,
                arrayOf(TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE),
                null,
                null,
                null,
                null,
                TaskContract.TaskEntry.COL_LAST_UPDATE + " DESC",
                "$offset,$limit"
        )
        try {
            while (cursor.moveToNext()) {
                val idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)
                taskList.add(cursor.getString(idx))
            }
        } finally {
            cursor.close()
            db.close()
        }

        return taskList
    }
}