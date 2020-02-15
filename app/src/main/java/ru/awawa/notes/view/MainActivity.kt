package ru.awawa.notes.view


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_dialog_new_note.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { addNewNote() }

        val rvNotes = findViewById<RecyclerView>(R.id.rvNotes)
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapter
        presenter.loadNotes()
    }


    private fun addNewNote() {
        val view = this@MainActivity.layoutInflater.inflate(
            R.layout.layout_dialog_new_note,
            null
        )
        AlertDialog.Builder(this@MainActivity)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _-> run {
                val note = Note(
                    title = view.etTitle.text.toString(),
                    description = view.etDescription.text.toString(),
                    date = Date().time
                )
                presenter.addNewNote(note)
            }}
            .setNegativeButton(android.R.string.cancel) { _, _-> run {}}
            .create()
            .show()
    }

    private fun changeTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }


    override fun updateNotesList(notes: List<Note>) {
        GlobalScope.launch(Dispatchers.Main){
            for (note in notes) adapter.addNote(note)
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
}
