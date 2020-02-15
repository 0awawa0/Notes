package ru.awawa.notes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface NoteDao {

    @Query("SELECT * FROM Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(entity = Note::class)
    fun addNewNote(note: Note)

    @Query("DELETE FROM Note WHERE id=:id")
    fun deleteNote(id: Int)

    @Query("DELETE FROM Note")
    fun deleteAllNotes()
}