package ru.awawa.notes.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.awawa.notes.database.Note
import ru.awawa.notes.model.MainModel
import ru.awawa.notes.view.MainActivity
import ru.awawa.notes.MainContract

class MainPresenter(private val view: MainActivity):
    MainContract.Presenter {

    private val model = MainModel()

    private val notes: LiveData<List<Note>> by lazy {
        model.loadAllNotes(view.applicationContext)
    }

    override fun addNewNote(note: Note) {
        model.addNewNote(view.applicationContext, note)
    }

    override fun loadNotes() {
        notes.observe(view, Observer {
            run {
            view.updateNotesList(it)
        }})
    }

    override fun deleteNote(id: Int) { model.deleteNote(view, id) }
}