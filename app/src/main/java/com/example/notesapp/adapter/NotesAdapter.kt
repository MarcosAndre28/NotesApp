package com.example.notesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.models.model.Note
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener : notesItemClickListerner) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val noteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun updateList(newList : List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        noteList.clear()
        noteList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search : String){
        noteList.clear()

        for (item in fullList){
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true){
                noteList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun randomColor() : Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(),null))

        holder.notes_layout.setOnClickListener {
            listener.onItemCLicked(noteList[holder.adapterPosition])
        }

        holder.notes_layout.setOnClickListener {
            listener.onLongItemClicked(noteList[holder.adapterPosition],holder.notes_layout)
            true
        }

    }

    inner class NotesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val note = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface notesItemClickListerner{
        fun onItemCLicked(note: Note)
        fun onLongItemClicked(note : Note, cardView: CardView)
    }
}