/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.tests

import kotlinx.uuid.*
import kotlin.test.*

class EncodingTest {
    @Test
    fun testEncodeToByteArray() {
        val bytes = UUID(SOME_UUID_STRING).encodeToByteArray()
        assertEquals(
            SOME_UUID_STRING.explodeToBytes(),
            bytes.asList()
        )
    }

    @Test
    fun testEncodeToLongArray() {
        val uuid = UUID(SOME_UUID_STRING)
        val longValues = uuid.encodeToLongArray()
        assertEquals(uuid.timeStampAndVersionRaw, longValues[0])
        assertEquals(uuid.clockSequenceVariantAndNodeRaw, longValues[1])
    }

    @Test
    fun testCreateFromByteArray() {
        val uuid = UUID(SOME_UUID_STRING.explodeToBytes().toByteArray())
        assertEquals(UUID(SOME_UUID_STRING), uuid)
    }

    @Test
    fun testCreateFromWrongByteArray() {
        assertFailsWith<IllegalArgumentException> {
            UUID(ByteArray(1))
        }
    }

    @Test
    fun testCreateFromLongArray() {
        val original = UUID(SOME_UUID_STRING)
        val array = original.let {
            longArrayOf(it.timeStampAndVersionRaw, it.clockSequenceVariantAndNodeRaw)
        }

        val uuid = UUID(array)
        assertEquals(original, uuid)
    }

    @Test
    fun testCreateFromWrongLongArray() {
        assertFailsWith<IllegalArgumentException> {
            UUID(LongArray(1))
        }
    }
}

internal fun String.explodeToBytes(): List<Byte> {
    return replace("-", "")
        .windowed(2, 2) {
            it.toString().toInt(radix = 16).toByte()
        }
}
