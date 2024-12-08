package app.softwork.uuid.sqldelight

import app.cash.sqldelight.ColumnAdapter
import kotlin.uuid.Uuid

public data object UuidStringAdapter : ColumnAdapter<Uuid, String> {
    override fun decode(databaseValue: String): Uuid = Uuid.parse(databaseValue)
    override fun encode(value: Uuid): String = value.toString()
}
