package kotlinx.uuid.sqldelight

import app.cash.sqldelight.*
import kotlin.uuid.Uuid

public object UuidStringAdapter : ColumnAdapter<Uuid, String> {
    override fun decode(databaseValue: String): Uuid = Uuid.parse(databaseValue)
    override fun encode(value: Uuid): String = value.toString()
}
