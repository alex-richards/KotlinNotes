package com.alex.kotlinrxtest.notes

import android.content.Context
import com.alex.kotlinrxtest.notes.storage.NotesDatabase
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import dagger.Module
import dagger.Provides
import rx.schedulers.Schedulers
import javax.inject.Singleton

@Module
class NotesModule {

    @Provides
    @Singleton
    fun provideNotesBriteDatabase(notesDatabase: NotesDatabase): BriteDatabase {
        val notesBriteDatabase: BriteDatabase = SqlBrite.create().wrapDatabaseHelper(notesDatabase, Schedulers.io())
        notesBriteDatabase.setLoggingEnabled(true)
        return notesBriteDatabase
    }

    @Provides
    @Singleton
    fun provideNotesDatabase(context: Context): NotesDatabase = NotesDatabase(context)

}