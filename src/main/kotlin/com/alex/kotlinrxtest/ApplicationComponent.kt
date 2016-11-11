package com.alex.kotlinrxtest

import android.content.Context
import com.alex.kotlinrxtest.notes.NotesModule
import com.alex.kotlinrxtest.notes.ui.NoteFragment
import com.alex.kotlinrxtest.notes.ui.NotesFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationComponent.ApplicationModule::class,
        NotesModule::class
))
interface ApplicationComponent {

    fun inject(notesView: NotesFragment)

    fun inject(noteFragment: NoteFragment)

    @Module
    class ApplicationModule(context: Context) {

        val applicationContext: Context = context.applicationContext

        @Provides
        @Singleton
        fun provideApplicationContext(): Context = applicationContext
    }
}
