package com.alex.kotlinrxtest.notes.storage

import com.google.auto.value.AutoValue

@AutoValue
abstract class NoteRevision : NoteRevisionModel {

    companion object {

        private val creator: NoteRevisionModel.Creator<NoteRevision> = NoteRevisionModel.Creator<NoteRevision>(::AutoValue_NoteRevision)

        private val uuidColumnAdapter = UUIDColumnAdapter()
        private val instantColumnAdapter = InstantColumnAdapter()

        val factory = NoteRevisionModel.Factory<NoteRevision>(creator, uuidColumnAdapter, instantColumnAdapter)

    }

}
