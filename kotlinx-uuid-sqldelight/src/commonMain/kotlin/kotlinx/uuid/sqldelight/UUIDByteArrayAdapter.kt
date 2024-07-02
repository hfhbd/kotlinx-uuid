package kotlinx.uuid.sqldelight

import app.cash.sqldelight.*
import kotlin.uuid.Uuid

public object UUIDByteArrayAdapter : ColumnAdapter<Uuid, ByteArray> {
    override fun decode(databaseValue: ByteArray): Uuid = Uuid.fromByteArray(databaseValue)
    override fun encode(value: Uuid): ByteArray = value.toByteArray()
}
