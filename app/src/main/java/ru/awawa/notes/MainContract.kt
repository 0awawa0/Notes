package ru.awawa.notes

import android.content.Context
import androidx.lifecycle.LiveData
import ru.awawa.notes.database.Note

public interface MainContract {

    interface Model {
        fun loadAllNotes(context: Context): LiveData<List<Note>>
        fun addNewNote(context: Context, note: Note)
        fun deleteNote(context: Context, id: Int)
    }

    interface View {
        fun updateNotesList(notes: List<Note>)
    }

    interface Presenter {
        fun loadNotes()
        fun addNewNote(note: Note)
        fun deleteNote(id: Int)
    }
}