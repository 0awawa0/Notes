package ru.awawa.notes.view


import android.animation.ObjectAnimator
import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_dialog_new_note.view.*
import kotlinx.coroutines.*
import ru.awawa.notes.MainContract
import ru.awawa.notes.R
import ru.awawa.notes.database.Note
import ru.awawa.notes.presenter.MainPresenter
import java.util.*


class MainActivity : AppCompatActivity(), MainContract.View {

    private val presenter: MainPresenter = MainPresenter(this)

    inner class CustomClickListener : MainRvAdapter.ClickListener {
        override fun onClick(view: View) {
            AlertDialog.Builder(this@MainActivity)
                .setTitle(R.string.deleting_note_dialog_title)
                .setMessage(R.string.deleting_note_dialog_description)
                .setPositiveButton(android.R.string.yes) { _, _ -> run {
                    presenter.deleteNote(view.id)
                    adapter.deleteNote(view.id)
                }}
                .setNegativeButton(android.R.string.cancel) { dialog, _ -> run {
                    dialog.cancel()
                }}
                .create()
                .show()
        }
    }

    private val clickListener = CustomClickListener()
    private val adapter = MainRvAdapter(clickListener)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { addNewNote() }

        val size = Point()
        windowManager.defaultDisplay.getSize(size)

        rvNotes.setItemViewCacheSize(20)
        rvNotes.isNestedScrollingEnabled = false
        rvNotes.layoutManager = PreCachedLayoutManager(this, size.y)
        rvNotes.adapter = adapter

        rvNotes.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val layoutManager = recyclerView.layoutManager
                        as PreCachedLayoutManager
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (lastVisible == adapter.itemCount - 1
                    && firstVisible != 0
                    &&  fab.alpha > 0
                    && adapter.itemCount != 0
                ) {
                    val objectAnimator = ObjectAnimator.ofFloat(
                        fab,
                        View.ALPHA,
                        0f
                    )
                    objectAnimator.duration = 500
                    objectAnimator.start()
                } else {
                    if (fab.alpha < 1) {
                        val objectAnimator = ObjectAnimator.ofFloat(
                            fab,
                            View.ALPHA,
                            1f
                        )
                        objectAnimator.duration = 500
                        objectAnimator.start()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        presenter.loadNotes()
    }


    override suspend fun updateNotesList(notes: List<Note>) {
        withContext(Dispatchers.Main) {
            if (notes.isEmpty()) {
                tvEmptyList.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            } else {
                tvEmptyList.visibility = View.GONE
            }
            for (note in notes) {
                adapter.addNote(note)
            }
            (rvNotes.layoutManager as PreCachedLayoutManager).setExtraLayoutSpace(1)
            loadingPanel.visibility = View.GONE
            fab.visibility = View.VISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        changeTheme()
        return true
    }


    private fun changeTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }


    private fun addNewNote() {
        val view = this@MainActivity.layoutInflater.inflate(
            R.layout.layout_dialog_new_note,
            null
        )
        AlertDialog.Builder(this@MainActivity)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _-> run {
                if (view.etTitle.text.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.empty_title_error,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val note = Note(
                        title = view.etTitle.text.toString(),
                        description = view.etDescription.text.toString(),
                        date = Date().time
                    )
                    presenter.addNewNote(note)
                }
            }}
            .setNegativeButton(android.R.string.cancel) { dialog, _-> run { dialog.cancel() }}
            .create()
            .show()
    }
}
