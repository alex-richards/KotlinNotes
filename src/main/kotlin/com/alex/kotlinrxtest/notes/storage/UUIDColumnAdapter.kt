package com.alex.kotlinrxtest.notes.storage

import com.squareup.sqldelight.ColumnAdapter
import java.util.UUID

class UUIDColumnAdapter : ColumnAdapter<UUID, String> {

    override fun encode(value: UUID): String = value.toString()

    override fun decode(databaseValue: String?): UUID = UUID.fromString(databaseValue)
    
}