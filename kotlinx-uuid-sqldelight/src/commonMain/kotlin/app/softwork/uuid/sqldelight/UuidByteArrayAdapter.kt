package app.softwork.uuid.sqldelight

import app.cash.sqldelight.ColumnAdapter
import kotlin.uuid.Uuid

public data object UuidByteArrayAdapter : ColumnAdapter<Uuid, ByteArray> {
    override fun decode(databaseValue: ByteArray): Uuid = Uuid.fromByteArray(databaseValue)
    override fun encode(value: Uuid): ByteArray = value.toByteArray()
}
