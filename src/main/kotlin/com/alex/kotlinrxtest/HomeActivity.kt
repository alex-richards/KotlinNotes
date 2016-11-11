package com.alex.kotlinrxtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alex.kotlinrxtest.notes.storage.NoteRevision
import com.alex.kotlinrxtest.notes.ui.NoteFragment
import com.alex.kotlinrxtest.notes.ui.NotesFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), NotesFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        setSupportActionBar(view_toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(view_container.id, NotesFragment(), NotesFragment::class.simpleName)
                    .commit()
        }
    }

    override fun onNoteChosen(note: NoteRevision) {
        supportFragmentManager.beginTransaction()
                .replace(view_container.id, NoteFragment.new(note.uuid()), NoteFragment::class.simpleName)
                .addToBackStack(null)
                .commit()
    }
}
