package com.alex.kotlinrxtest.notes.storage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, "NotesDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(NoteRevisionModel.CREATE_TABLE)
        db.execSQL(NoteRevisionModel.CREATE_LATEST_NOTE_REVISIONS_VIEW)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // empty
    }
}

