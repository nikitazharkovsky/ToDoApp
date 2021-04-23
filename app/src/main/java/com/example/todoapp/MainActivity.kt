package com.example.todoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R.string
import com.example.todoapp.db.TaskDbHelper
import com.example.todoapp.list.EndlessRecyclerViewScrollListener
import com.example.todoapp.list.ListAdapter
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity(), IMainActivity {

    private lateinit var dbHelper: TaskDbHelper
    private lateinit var listAdapter: ListAdapter
    private lateinit var dialogHelper: DialogHelper

    private lateinit var listEmptyTextView: TextView
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private lateinit var progressBar: ProgressBar
    private lateinit var loadMoreProgressBar: ProgressBar
    private val stopLoadMoreProgressBar = Runnable { loadMoreProgressBar.visibility = ProgressBar.GONE }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listEmptyTextView = findViewById(R.id.list_empty_text_view)
        progressBar = findViewById(R.id.progress_bar)
        loadMoreProgressBar = findViewById(R.id.load_more_progress_bar)
        var recyclerView = findViewById<RecyclerView>(R.id.list_todo)

        dbHelper = TaskDbHelper(this)
        dialogHelper = DialogHelper(progressBar, dbHelper, this)

        listAdapter = ListAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadNextDataFromDb(page)
            }
        }
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = listAdapter
        }

        progressBar.visibility = ProgressBar.GONE
        loadMoreProgressBar.visibility = ProgressBar.VISIBLE

        Timer().schedule(timerTask {
            updateUI()
            recyclerView.addOnScrollListener(scrollListener)
            runOnUiThread(stopLoadMoreProgressBar)
        }, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                openAddDialog()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openAddDialog() {
        // Create view
        val taskEditText = EditText(this)
        val exceptionTextView = TextView(this)
        exceptionTextView.setTextColor(resources.getColor(R.color.red))

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(taskEditText)
        linearLayout.addView(exceptionTextView)
        linearLayout.setPadding(50, 0, 50, 0)

        // Create dialog
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(string.add_task))
                .setMessage(getString(string.question_on_add_task))
                .setView(linearLayout)
                .setPositiveButton(getString(string.add)) { _, _ -> }
                .setNegativeButton(getString(string.cancel), null)
                .create()

        dialog.setOnShowListener {
            dialogHelper.saveDialogAction(dialog, taskEditText, exceptionTextView)
        }

        dialog.show()
    }

    fun openEditDialog(view: View) {
        // Create view
        val parent = view.parent.parent as View
        val taskTextView = parent.findViewById<TextView>(R.id.task_title)
        val originalTask = taskTextView.text.toString()

        val taskEditText = EditText(this)
        taskEditText.setText(originalTask)
        val exceptionTextView = TextView(this)
        exceptionTextView.setTextColor(resources.getColor(R.color.red))

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(taskEditText)
        linearLayout.addView(exceptionTextView)
        linearLayout.setPadding(50, 0, 50, 0)

        // Create dialog
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(string.edit))
                .setView(linearLayout)
                .setPositiveButton(getString(string.save)) { _, _ -> }
                .setNegativeButton(string.cancel, null)
                .create()

        dialog.setOnShowListener {
            dialogHelper.editDialogAction(dialog, taskEditText, exceptionTextView, originalTask)
        }

        dialog.show()
    }

    fun openDeleteDialog(view: View) {
        val parent = view.parent.parent as View
        val taskTextView = parent.findViewById<TextView>(R.id.task_title)
        val task = taskTextView.text.toString()

        val textView = TextView(this)
        textView.text = getString(string.confirm_delete)
        textView.setPadding(50, 0, 50, 0)

        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(string.edit))
                .setView(textView)
                .setPositiveButton(getString(string.save)) { _, _ -> }
                .setNegativeButton(string.cancel, null)
                .create()

        dialog.setOnShowListener {
            dialogHelper.deleteDialogAction(dialog, task)
        }

        dialog.show()
    }


    override fun updateUI() {
        val taskList = dbHelper.loadPage(0, PAGE_SIZE)
        listAdapter.clear()
        listAdapter.add(taskList)

        scrollListener.resetState()
        runOnUiThread {
            listAdapter.notifyDataSetChanged()
            if (taskList.isEmpty()) {
                listEmptyTextView.text = getString(string.no_tasks)
            } else {
                listEmptyTextView.text = ""
            }
        }
    }

    private fun loadNextDataFromDb(offset: Int) {
        loadMoreProgressBar.visibility = ProgressBar.VISIBLE
        Timer().schedule(timerTask {
            val taskList = dbHelper.loadPage((offset) * PAGE_SIZE, PAGE_SIZE)
            if (taskList.size > 0) {
                listAdapter.add(taskList)
                runOnUiThread { listAdapter.notifyDataSetChanged() }
            }
            runOnUiThread(stopLoadMoreProgressBar)
        }, 1000)
    }

    companion object {
        const val PAGE_SIZE: Int = 10
    }
}
