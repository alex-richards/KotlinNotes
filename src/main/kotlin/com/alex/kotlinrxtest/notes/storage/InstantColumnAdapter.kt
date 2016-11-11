package com.alex.kotlinrxtest.notes.storage

import com.squareup.sqldelight.ColumnAdapter
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter

class InstantColumnAdapter : ColumnAdapter<Instant, String> {

    override fun encode(value: Instant): String = DateTimeFormatter.ISO_INSTANT.format(value)

    override fun decode(databaseValue: String): Instant = Instant.parse(databaseValue)

}