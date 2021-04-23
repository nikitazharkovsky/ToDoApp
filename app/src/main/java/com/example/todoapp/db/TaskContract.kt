package com.example.todoapp.db

import android.provider.BaseColumns

class TaskContract {
    companion object {
        const val DB_NAME = "com.zharkovsky.todo.db"
        const val DB_VERSION = 2
    }

    class TaskEntry : BaseColumns {

        companion object {
            const val TABLE = "tasks"
            const val COL_TASK_TITLE = "title"
            const val COL_LAST_UPDATE = "last_update"
            const val _ID = BaseColumns._ID
        }
    }
}