package kotlinx.uuid.sqldelight

import com.squareup.sqldelight.*
import kotlinx.uuid.*

public object UUIDStringAdapter : ColumnAdapter<UUID, String> {
    override fun decode(databaseValue: String): UUID = UUID(databaseValue)
    override fun encode(value: UUID): String = value.toString(false)
}
