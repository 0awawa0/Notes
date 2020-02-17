package ru.awawa.notes.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Note")
data class Note(
    @ColumnInfo(name="title") var title: String,
    @ColumnInfo(name="description") var description: String,
    @ColumnInfo(name="date") var date: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}