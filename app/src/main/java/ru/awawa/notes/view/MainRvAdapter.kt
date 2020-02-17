package ru.awawa.notes.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.awawa.notes.R
import ru.awawa.notes.database.Note

class MainRvAdapter(private val clickListener: ClickListener)
    : RecyclerView.Adapter<MainRvAdapter.MainViewHolder>() {

    private val dataSet = ArrayList<Note>()

    fun addNote(note: Note) {
        for (n in dataSet) if (n.id == note.id) return

        dataSet.add(0, note)
        notifyItemInserted(0)
    }

    fun deleteNote(id: Int) {
        val note = dataSet.find { note -> note.id == id }
        if (note != null) {
            val index = dataSet.indexOf(note)
            dataSet.remove(note)
            notifyItemRemoved(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_note_record, parent, false)
        return MainViewHolder(view)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val note = dataSet[position]
        holder.view.id = note.id
        holder.btDelete.id = note.id
        holder.tvTitle.text = note.title
        holder.tvDescription.text = note.description
    }

    override fun getItemCount(): Int { return dataSet.size }

    inner class MainViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        val btDelete = view.findViewById<ImageButton>(R.id.btDelete)!!
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)!!
        val tvDescription= view.findViewById<TextView>(R.id.tvDescription)!!

        init {
            view.findViewById<ImageButton>(R.id.btDelete).setOnClickListener { view ->
                run {
                    clickListener.onClick(view)
                }
            }
        }
    }

    interface ClickListener { fun onClick(view: View) }
}