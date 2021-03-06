package com.alex.kotlinrxtest.notes.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.alex.kotlinrxtest.R
import com.alex.kotlinrxtest.applicationComponent
import com.alex.kotlinrxtest.getListener
import com.alex.kotlinrxtest.notes.storage.NoteRevision
import com.alex.kotlinrxtest.notes.storage.NoteRevisionModel
import com.alex.kotlinrxtest.uiThrottle
import com.jakewharton.rxbinding.support.v7.widget.dataChanges
import com.jakewharton.rxbinding.view.clicks
import com.squareup.sqlbrite.BriteDatabase
import kotlinx.android.synthetic.main.fragment_notes.*
import org.threeten.bp.Instant
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.UUID
import javax.inject.Inject

class NotesFragment : Fragment() {

    @Inject
    lateinit var notesDatabase: BriteDatabase

    @Inject
    lateinit var insertNewNote: NoteRevisionModel.InsertNewNote

    var notesSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.applicationComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notesAdapter = NotesAdapter()
        view_list.adapter = notesAdapter

        notesAdapter.dataChanges().subscribe({
            if (isResumed) {
                view_empty.visibility = if (notesAdapter.itemCount == 0) View.VISIBLE else View.GONE
            }
        })

        notesAdapter.noteChosen().subscribe({ note ->
            getListener(Listener::class.java).onNoteChosen(note)
        })
    }

    override fun onResume() {
        super.onResume()

        notesSubscription = notesDatabase
                .createQuery(NoteRevisionModel.TABLE_NAME, NoteRevisionModel.SELECTLATESTNOTEREVISIONS)
                .mapToList({ cursor -> NoteRevision.factory.selectLatestNoteRevisionsMapper().map(cursor) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list -> (view_list.adapter as NotesAdapter).notes = list })
    }

    override fun onPause() {
        super.onPause()

        notesSubscription?.unsubscribe()
        notesSubscription = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_notes, menu)

        menu.findItem(R.id.menu_new)
                .clicks()
                .uiThrottle()
                .observeOn(Schedulers.io())
                .subscribe({
                    with(insertNewNote) {
                        bind(UUID.randomUUID(), Instant.now(), getString(R.string.note_default_title))
                        notesDatabase.executeInsert(table, program)
                    }
                })
    }

    interface Listener {

        fun onNoteChosen(note: NoteRevision)

    }
}