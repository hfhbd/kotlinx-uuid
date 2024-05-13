/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.vendors.*
import java.nio.*

/**
 * A [UUID] column type for registering in exposed tables.
 * @see kotlinxUUID to see how it is used
 */
public class UUIDColumnType : ColumnType<UUID>() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.uuidType()

    override fun valueFromDB(value: Any): UUID = when {
        value is java.util.UUID -> value.toKotlinUUID()
        value is UUID -> value
        value is ByteArray -> ByteBuffer.wrap(value).let { b -> valueFromDB(java.util.UUID(b.long, b.long)) }
        value is String && UUID.isValidUUIDString(value) -> UUID(value)
        value is String -> valueFromDB(value.toByteArray())
        else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
    }

    override fun notNullValueToDB(value: UUID): Any = currentDialect.dataTypeProvider.uuidToDB(valueToUUID(value))

    override fun nonNullValueToString(value: UUID): String = "'${valueToUUID(value)}'"

    internal fun valueToUUID(value: Any): java.util.UUID = when (value) {
        is java.util.UUID -> value
        is UUID -> value.toJavaUUID()
        is String -> java.util.UUID.fromString(value)
        is ByteArray -> ByteBuffer.wrap(value).let { java.util.UUID(it.long, it.long) }
        else -> error("Unexpected value of type UUID: ${value.javaClass.canonicalName}")
    }
}
