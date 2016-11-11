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
import com.alex.kotlinrxtest.and
import com.alex.kotlinrxtest.applicationComponent
import com.alex.kotlinrxtest.notes.storage.NoteRevision
import com.alex.kotlinrxtest.notes.storage.NoteRevisionModel
import com.alex.kotlinrxtest.set
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.afterTextChangeEvents
import com.squareup.sqlbrite.BriteDatabase
import kotlinx.android.synthetic.main.adapter_view_note.*
import org.threeten.bp.Instant
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NoteFragment : Fragment() {

    companion object {
        fun new(uuid: UUID): NoteFragment {
            val new = NoteFragment()

            new.arguments = Bundle()
                    .set("uuid" to uuid)

            return new
        }
    }

    lateinit var uuid: UUID

    @Inject
    lateinit var notesDatabase: BriteDatabase

    @Inject
    lateinit var insertNewRevision: NoteRevisionModel.InsertNewRevision

    @Inject
    lateinit var insertNewSnapshot: NoteRevisionModel.InsertNewSnapshot

    @Inject
    lateinit var deleteOldRevisions: NoteRevisionModel.DeleteOldNoteRevisions

    var textChangedSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        uuid = arguments["uuid"] as UUID
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.applicationComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onResume() {
        super.onResume()

        notesDatabase.createQuery(NoteRevisionModel.TABLE_NAME,
                NoteRevisionModel.SELECTLATESTNOTEREVISIONBYUUID, uuid.toString())
                .mapToOne({ cursor -> NoteRevision.factory.selectLatestNoteRevisionByUuidMapper().map(cursor) })
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ note ->
                    view_name.text = note.name()
                    view_content.text = note.content()

                    textChangedSubscription = Observable.merge(
                            view_name.afterTextChangeEvents(),
                            view_content.afterTextChangeEvents())
                            .throttleLast(2, TimeUnit.SECONDS)
                            .map { view_name.text.toString() and view_content.text.toString() }
                            .observeOn(Schedulers.io())
                            .subscribe({ values ->
                                with(insertNewRevision) {
                                    bind(uuid, Instant.now(), values.first, values.second)
                                    notesDatabase.executeInsert(table, program)
                                }
                            })
                })
    }

    override fun onPause() {
        super.onPause()

        textChangedSubscription?.unsubscribe()
        textChangedSubscription = null

        Observable.just(view_name.text.toString() and view_content.text.toString())
                .observeOn(Schedulers.io())
                .subscribe({ values ->
                    with(notesDatabase.newTransaction()) {
                        try {
                            with(insertNewRevision) {
                                bind(uuid, Instant.now(), values.first, values.second)
                                notesDatabase.executeInsert(table, program)
                            }

                            with(deleteOldRevisions) {
                                bind(uuid)
                                notesDatabase.executeUpdateDelete(table, program)
                            }

                            markSuccessful()
                        } finally {
                            end()
                        }
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_note, menu)

        menu.findItem(R.id.menu_snapshot)
                .clicks()
                .debounce(800, TimeUnit.MILLISECONDS)
                .map { view_name.text.toString() and view_content.text.toString() }
                .observeOn(Schedulers.io())
                .subscribe({ values ->
                    with(insertNewSnapshot) {
                        bind(uuid, Instant.now(), values.first, values.second)
                        notesDatabase.executeInsert(table, program)
                    }
                })
    }
}