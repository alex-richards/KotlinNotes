package com.alex.kotlinrxtest.notes.storage

import android.content.ContentValues
import android.database.Cursor
import com.squareup.sqldelight.ColumnAdapter
import java.util.UUID

class UUIDColumnAdapter : ColumnAdapter<UUID> {

    override fun map(cursor: Cursor, columnIndex: Int): UUID = UUID.fromString(cursor.getString(columnIndex))

    override fun marshal(values: ContentValues, key: String, value: UUID?) = values.put(key, value?.toString())
    
}