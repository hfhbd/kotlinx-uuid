package kotlinx.uuid.sqldelight

import app.cash.sqldelight.*
import kotlinx.uuid.*
import kotlin.uuid.Uuid

public object UUIDStringAdapter : ColumnAdapter<Uuid, String> {
    override fun decode(databaseValue: String): Uuid = Uuid.parse(databaseValue)
    override fun encode(value: Uuid): String = value.toString()
}
