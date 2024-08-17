/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.random.Random
import kotlin.test.*
import kotlin.uuid.Uuid

private const val UUID_STRING_ALL_FF: String = "ffffffff-ffff-ffff-ffff-ffffffffffff"
private const val UUID_STRING: String = "1b3e4567-e99b-13d3-a476-446657420000"
internal const val SOME_UUID_STRING: String = "1b3e4567-e99b-13d3-a476-446657420000"

class UUIDTest {
    @Test
    fun testZero() {
        assertEquals(0, Uuid.NIL.variant)
        assertEquals(0, Uuid.NIL.versionNumber)
        assertEquals(0, Uuid.NIL.timeStamp)
        assertEquals(0, Uuid.NIL.clockSequence)
        assertEquals(0, Uuid.NIL.node)
        assertEquals(false, Uuid.NIL.isRfcVariant)
    }

    @Test
    fun testConstructingFromString() {
        val uuid = Uuid.parse(UUID_STRING)

        assertEquals(1, uuid.versionNumber)
        assertEquals(5, uuid.variant)
        assertEquals("3d3e99b1b3e4567", uuid.timeStamp.toString(16))
        assertEquals("476", uuid.clockSequence.toString(16))
        assertEquals("446657420000", uuid.node.toString(16))

        assertEquals(uuid, UUID_STRING.toUuid())
        assertEquals(uuid, UUID_STRING.toUUIDOrNull())
    }

    @Test
    fun testConstructingFromStringAllFf() {
        val uuid = Uuid.parse(UUID_STRING_ALL_FF)

        assertEquals(0xf, uuid.versionNumber)
        assertEquals(7, uuid.variant)
        assertEquals("fffffffffffffff", uuid.timeStamp.toString(16))
        assertEquals("1fff", uuid.clockSequence.toString(16))
        assertEquals("ffffffffffff", uuid.node.toString(16))
    }

    @Test
    fun testToString() {
        val uuid = Uuid.parse(UUID_STRING)
        assertEquals(UUID_STRING, uuid.toString())
    }

    @Test
    fun testConstructingFromStringValid() {
        val combined = setOf(
            Uuid.parse("1b3e4567-e99b-13d3-a476-446657420000 "),
            Uuid.parse("1b3e4567-e99b-13d3-a476 - 446657420000"),
            Uuid.parse(" 1b3e4567-e99b-13d3-a476 - 446657420000"),
            Uuid.parse(" { 1b3e4567-e99b-13d3-a476 - 446657420000}"),
            Uuid.parse("{1b3e4567-e99b-13d3-a476 - 446657420000}")
        )

        assertEquals(1, combined.size)
    }

    @Test
    fun testConstructingFromComponents() {
        val first = Uuid.parse(SOME_UUID_STRING)
        val second = Uuid.from(
            timeStamp = first.timeStamp,
            versionNumber = first.versionNumber,
            clockSequence = first.clockSequence,
            node = first.node,
            variant = first.variant
        )

        assertEquals(first, second)
    }

    @Test
    fun testConstructingFromComponentsDefaultVariant() {
        val first = Uuid.parse(SOME_UUID_STRING)
        val second = Uuid.from(
            timeStamp = first.timeStamp,
            versionNumber = first.versionNumber,
            clockSequence = first.clockSequence,
            node = first.node
        )

        assertEquals(first, second)
    }

    @Test
    fun testConstructingFromComponentsAllFf() {
        val first = Uuid.parse(UUID_STRING_ALL_FF)
        val second = Uuid.from(
            timeStamp = first.timeStamp,
            versionNumber = first.versionNumber,
            clockSequence = first.clockSequence,
            node = first.node,
            variant = first.variant
        )

        assertEquals(first, second)

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(100, first.timeStamp, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(1, first.timeStamp, first.clockSequence, first.node, 100)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(1, Long.MAX_VALUE, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(first.versionNumber, first.timeStamp, Int.MAX_VALUE, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(first.versionNumber, first.timeStamp, first.clockSequence, Long.MAX_VALUE, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(-1, first.timeStamp, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(first.versionNumber, -1, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(first.versionNumber, first.timeStamp, -1, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(first.versionNumber, first.timeStamp, first.clockSequence, -1, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            Uuid.from(first.versionNumber, first.timeStamp, first.clockSequence, first.node, -1)
        }
    }

    @Test
    fun testVariants() {
        assertEquals(0, Uuid.parse("1b3e4567-e99b-13d3-0476-446657420000").variant)
        assertEquals(1, Uuid.parse("1b3e4567-e99b-13d3-2476-446657420000").variant)
        assertEquals(2, Uuid.parse("1b3e4567-e99b-13d3-4476-446657420000").variant)
        assertEquals(3, Uuid.parse("1b3e4567-e99b-13d3-6476-446657420000").variant)
        assertEquals(4, Uuid.parse("1b3e4567-e99b-13d3-8476-446657420000").variant)
        assertEquals(5, Uuid.parse("1b3e4567-e99b-13d3-a476-446657420000").variant)
        assertEquals(7, Uuid.parse("1b3e4567-e99b-13d3-e476-446657420000").variant)
    }

    @Test
    fun testVersionNumbers() {
        assertEquals(1, Uuid.parse("1b3e4567-e99b-13d3-a476-446657420000").versionNumber)
        assertEquals(2, Uuid.parse("1b3e4567-e99b-23d3-a476-446657420000").versionNumber)
        assertEquals(3, Uuid.parse("1b3e4567-e99b-33d3-a476-446657420000").versionNumber)
        assertEquals(4, Uuid.parse("1b3e4567-e99b-43d3-a476-446657420000").versionNumber)
        assertEquals(5, Uuid.parse("1b3e4567-e99b-53d3-a476-446657420000").versionNumber)
        assertEquals(0xf, Uuid.parse("1b3e4567-e99b-f3d3-a476-446657420000").versionNumber)
    }

    @Test
    fun testIsValidString() {
        assertTrue(Uuid.isValidUUIDString(SOME_UUID_STRING))
        assertTrue(Uuid.isValidUUIDString("{$SOME_UUID_STRING}"))
        assertTrue(Uuid.isValidUUIDString(" {$SOME_UUID_STRING}"))
        assertTrue(Uuid.isValidUUIDString(" {$SOME_UUID_STRING} "))

        assertFalse(Uuid.isValidUUIDString(SOME_UUID_STRING.drop(1)))
        assertFalse(Uuid.isValidUUIDString(SOME_UUID_STRING.dropLast(1)))
        assertFalse(Uuid.isValidUUIDString(SOME_UUID_STRING.replace('b', 'X')))
        assertNull(SOME_UUID_STRING.drop(1).toUUIDOrNull())
    }

    @Test
    fun testRandomCreation() {
        assertEquals(4, Uuid.random().versionNumber)
        assertEquals(4, Random.nextUuid().versionNumber)
    }
}
