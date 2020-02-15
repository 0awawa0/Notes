package ru.awawa.notes.model

import android.content.Context
import androidx.lifecycle.LiveData
import ru.awawa.notes.database.Note
import ru.awawa.notes.database.NotesDatabase
import ru.awawa.notes.MainContract

class MainModel: MainContract.Model {

    override fun loadAllNotes(context: Context): LiveData<List<Note>> =
        NotesDatabase.getInstance(context).noteDao().getAllNotes()

    override fun addNewNote(context: Context, note: Note) =
        NotesDatabase.getInstance(context).noteDao().addNewNote(note)

    override fun deleteNote(context: Context, id: Int) {
        NotesDatabase.getInstance(context).noteDao().deleteNote(id)
    }
}