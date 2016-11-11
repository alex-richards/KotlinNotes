package com.alex.kotlinrxtest.notes.storage

import android.database.Cursor
import com.google.auto.value.AutoValue

@AutoValue
abstract class NoteRevision : NoteRevisionModel {

    companion object : NoteRevisionModel.Creator<NoteRevision> by NoteRevisionModel.Creator(::AutoValue_NoteRevision) {

        private val factory = NoteRevisionModel.Factory<NoteRevision>(this, UUIDColumnAdapter(), InstantColumnAdapter())

        private val mapper = NoteRevisionModel.Mapper<NoteRevision>(factory)

        fun map(cursor: Cursor): NoteRevision = mapper.map(cursor)

        fun marshal(): NoteRevisionModel.Marshal = factory.marshal()

    }

    fun marshal(): NoteRevisionModel.Marshal = factory.marshal(this)

}
