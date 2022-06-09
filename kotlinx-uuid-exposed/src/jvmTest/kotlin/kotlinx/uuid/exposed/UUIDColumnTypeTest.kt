/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import kotlinx.uuid.*
import kotlinx.uuid.exposed.*
import java.nio.*
import kotlin.test.*

class UUIDColumnTypeTest {
    private val type = UUIDColumnType()
    private val uuid = UUID.generateUUID()

    @Suppress("DEPRECATION_ERROR")
    private val uuidBytes = ByteBuffer.allocate(16)!!.apply {
        putLong(uuid.getMostSignificantBits())
        putLong(uuid.getLeastSignificantBits())
    }.array()

    @Test
    fun testConversionJava() {
        val result = type.valueFromDB(uuid.toJavaUUID())
        assertEquals(uuid, result)

        assertEquals(uuid, type.valueToUUID(uuid.toJavaUUID()).toKotlinUUID())
        assertEquals(uuid, type.valueToUUID(uuid).toKotlinUUID())
    }

    @Test
    fun testPassThrough() {
        assertEquals(uuid, type.valueFromDB(type.valueToUUID(uuid)))
    }

    @Test
    fun testByteArray() {
        assertEquals(uuid.toJavaUUID(), type.valueToUUID(uuidBytes))
        assertEquals(uuid, type.valueFromDB(uuidBytes))
    }

    @Test
    fun testString() {
        assertEquals(uuid.toJavaUUID(), type.valueToUUID(uuid.toString()))
        assertEquals(uuid, type.valueFromDB(uuid.toString()))
    }
}
