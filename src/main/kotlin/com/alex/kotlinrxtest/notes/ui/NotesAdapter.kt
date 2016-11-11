package com.alex.kotlinrxtest.notes.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alex.kotlinrxtest.R
import com.alex.kotlinrxtest.notes.storage.NoteRevision
import com.jakewharton.rxbinding.view.clicks
import kotlinx.android.synthetic.main.adapter_view_note.view.*
import rx.Observable

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.H> {

    constructor() : super() {
        setHasStableIds(true)
    }

    val noteChosenOnSubscribe = NoteChosenOnSubscribe()

    var notes: List<NoteRevision>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = notes?.size ?: 0

    fun getItem(position: Int): NoteRevision = notes!![position]

    override fun getItemId(position: Int): Long = getItem(position)._id()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H = H(LayoutInflater.from(parent.context).inflate(R.layout.adapter_view_note, parent, false))

    override fun onBindViewHolder(h: H, position: Int) {
        val note = getItem(position)

        h.name.text = note.name()
        h.content.text = note.content()

        h.itemView.clicks().subscribe({ noteChosenOnSubscribe.noteChosen(note) })
    }

    fun noteChosen(): Observable<NoteRevision> = Observable.create(noteChosenOnSubscribe)

    class H(view: View) : RecyclerView.ViewHolder(view) {

        val name: TextView = view.view_name
        val content: TextView = view.view_content

    }
}
