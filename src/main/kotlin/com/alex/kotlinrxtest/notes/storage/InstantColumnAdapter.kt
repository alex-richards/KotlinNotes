package com.alex.kotlinrxtest.notes.storage

import android.content.ContentValues
import android.database.Cursor
import com.squareup.sqldelight.ColumnAdapter
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter

class InstantColumnAdapter : ColumnAdapter<Instant> {

    override fun map(cursor: Cursor, columnIndex: Int): Instant = Instant.parse(cursor.getString(columnIndex))

    override fun marshal(values: ContentValues, key: String, value: Instant?) {
        if(value == null){
            values.putNull(key)
        }else{
            values.put(key, DateTimeFormatter.ISO_INSTANT.format(value))
        }
    }
}