/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.tests

import kotlinx.uuid.*
import kotlin.test.*

private const val UUID_STRING_ALL_FF: String = "ffffffff-ffff-ffff-ffff-ffffffffffff"
private const val UUID_STRING: String = "1b3e4567-e99b-13d3-a476-446657420000"
private const val UUID_STRING2: String = "1b3e4567-e99b-13d3-a476-446657420001"
private const val UUID_STRING3: String = "1b3e4568-e99b-13d3-a476-446657420000"

class UUIDTest {
    @Test
    fun testZero() {
        assertEquals(0, UUID.NIL.variant)
        assertEquals(0, UUID.NIL.versionNumber)
        assertNull(UUID.NIL.version)
        assertEquals(0, UUID.NIL.timeStamp)
        assertEquals(0, UUID.NIL.clockSequence)
        assertEquals(0, UUID.NIL.node)
        assertEquals(false, UUID.NIL.isRfcVariant)
    }

    @Test
    fun testConstructingFromString() {
        val uuid = UUID(UUID_STRING)

        assertEquals(1, uuid.versionNumber)
        assertEquals(UUID.Version.TIME_BASED, uuid.version)
        assertEquals(5, uuid.variant)
        assertEquals("3d3e99b1b3e4567", uuid.timeStamp.toString(16))
        assertEquals("476", uuid.clockSequence.toString(16))
        assertEquals("446657420000", uuid.node.toString(16))

        assertEquals(uuid, UUID_STRING.toUUID())
        assertEquals(uuid, UUID_STRING.toUUIDOrNull())
    }

    @Test
    fun testConstructingFromStringAllFf() {
        val uuid = UUID(UUID_STRING_ALL_FF)

        assertEquals(0xf, uuid.versionNumber)
        assertEquals(null, uuid.version)
        assertEquals(7, uuid.variant)
        assertEquals("fffffffffffffff", uuid.timeStamp.toString(16))
        assertEquals("1fff", uuid.clockSequence.toString(16))
        assertEquals("ffffffffffff", uuid.node.toString(16))
    }

    @Test
    fun testToString() {
        val uuid = UUID(UUID_STRING)
        assertEquals(UUID_STRING, uuid.toString())
        assertEquals("{1b3e4567-e99b-13d3-a476-446657420000}", uuid.toString(true))
    }

    @Test
    fun testConstructingFromStringValid() {
        val combined = setOf(
            UUID("1b3e4567-e99b-13d3-a476-446657420000 "),
            UUID("1b3e4567-e99b-13d3-a476 - 446657420000"),
            UUID(" 1b3e4567-e99b-13d3-a476 - 446657420000"),
            UUID(" { 1b3e4567-e99b-13d3-a476 - 446657420000}"),
            UUID("{1b3e4567-e99b-13d3-a476 - 446657420000}")
        )

        assertEquals(1, combined.size)
    }

    @Test
    fun testConstructingFromComponents() {
        val first = UUID(SOME_UUID_STRING)
        val second = UUID(
            timeStamp = first.timeStamp,
            versionNumber = first.versionNumber,
            clockSequence = first.clockSequence,
            node = first.node,
            variant = first.variant
        )

        assertEquals(first, second)
    }

    @Test
    fun testConstructingFromComponentsWithVersion() {
        val first = UUID(SOME_UUID_STRING)
        val second = UUID(
            timeStamp = first.timeStamp,
            version = first.version!!,
            clockSequence = first.clockSequence,
            node = first.node
        )

        assertEquals(first, second)
    }

    @Test
    fun testConstructingFromComponentsDefaultVariant() {
        val first = UUID(SOME_UUID_STRING)
        val second = UUID(
            timeStamp = first.timeStamp,
            versionNumber = first.versionNumber,
            clockSequence = first.clockSequence,
            node = first.node
        )

        assertEquals(first, second)
    }

    @Test
    fun testConstructingFromComponentsAllFf() {
        val first = UUID(UUID_STRING_ALL_FF)
        val second = UUID(
            timeStamp = first.timeStamp,
            versionNumber = first.versionNumber,
            clockSequence = first.clockSequence,
            node = first.node,
            variant = first.variant
        )

        assertEquals(first, second)

        assertFailsWith<IllegalArgumentException> {
            UUID(100, first.timeStamp, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(1, first.timeStamp, first.clockSequence, first.node, 100)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(1, Long.MAX_VALUE, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(first.versionNumber, first.timeStamp, Int.MAX_VALUE, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(first.versionNumber, first.timeStamp, first.clockSequence, Long.MAX_VALUE, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(-1, first.timeStamp, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(first.versionNumber, -1, first.clockSequence, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(first.versionNumber, first.timeStamp, -1, first.node, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(first.versionNumber, first.timeStamp, first.clockSequence, -1, first.variant)
        }

        assertFailsWith<IllegalArgumentException> {
            UUID(first.versionNumber, first.timeStamp, first.clockSequence, first.node, -1)
        }
    }

    @Test
    fun testComparison() {
        assertEquals(UUID(UUID_STRING), UUID(UUID_STRING))
        assertNotEquals(UUID(UUID_STRING), UUID(UUID_STRING2))
        assertNotEquals(UUID(UUID_STRING), UUID(UUID_STRING3))

        assertTrue(UUID(UUID_STRING) < UUID(UUID_STRING2))
        assertTrue(UUID(UUID_STRING) < UUID(UUID_STRING3))
        assertTrue(UUID(UUID_STRING3) > UUID(UUID_STRING2))

        assertEquals(
            listOf(UUID(UUID_STRING), UUID(UUID_STRING2), UUID(UUID_STRING3)),
            listOf(UUID(UUID_STRING2), UUID(UUID_STRING3), UUID(UUID_STRING)).sorted()
        )

        assertFalse(UUID(UUID_STRING).equals(UUID_STRING))
    }

    @Test
    fun testVariants() {
        assertEquals(0, UUID("1b3e4567-e99b-13d3-0476-446657420000").variant)
        assertEquals(1, UUID("1b3e4567-e99b-13d3-2476-446657420000").variant)
        assertEquals(2, UUID("1b3e4567-e99b-13d3-4476-446657420000").variant)
        assertEquals(3, UUID("1b3e4567-e99b-13d3-6476-446657420000").variant)
        assertEquals(4, UUID("1b3e4567-e99b-13d3-8476-446657420000").variant)
        assertEquals(5, UUID("1b3e4567-e99b-13d3-a476-446657420000").variant)
        assertEquals(7, UUID("1b3e4567-e99b-13d3-e476-446657420000").variant)
    }

    @Test
    fun testVersionNumbers() {
        assertEquals(1, UUID("1b3e4567-e99b-13d3-a476-446657420000").versionNumber)
        assertEquals(2, UUID("1b3e4567-e99b-23d3-a476-446657420000").versionNumber)
        assertEquals(3, UUID("1b3e4567-e99b-33d3-a476-446657420000").versionNumber)
        assertEquals(4, UUID("1b3e4567-e99b-43d3-a476-446657420000").versionNumber)
        assertEquals(5, UUID("1b3e4567-e99b-53d3-a476-446657420000").versionNumber)
        assertEquals(0xf, UUID("1b3e4567-e99b-f3d3-a476-446657420000").versionNumber)
    }

    @Test
    fun testVersions() {
        assertEquals(UUID.Version.TIME_BASED, UUID("1b3e4567-e99b-13d3-a476-446657420000").version)
        assertEquals(UUID.Version.DCE_SECURITY, UUID("1b3e4567-e99b-23d3-a476-446657420000").version)
        assertEquals(UUID.Version.NAME_BASED_MD5, UUID("1b3e4567-e99b-33d3-a476-446657420000").version)
        assertEquals(UUID.Version.RANDOM_BASED, UUID("1b3e4567-e99b-43d3-a476-446657420000").version)
        assertEquals(UUID.Version.NAME_BASED_SHA1, UUID("1b3e4567-e99b-53d3-a476-446657420000").version)
    }

    @Test
    @Suppress("DEPRECATION")
    fun testMigrationDeprecations() {
        assertEquals(UUID.fromString(SOME_UUID_STRING), UUID(SOME_UUID_STRING))

        @Suppress("DEPRECATION_ERROR")
        UUID.randomUUID()

        @Suppress("DEPRECATION_ERROR")
        UUID.nameUUIDFromBytes(byteArrayOf())

        @Suppress("DEPRECATION_ERROR")
        with(UUID(SOME_UUID_STRING)) {
            assertEquals(timeStampAndVersionRaw, getMostSignificantBits())
            assertEquals(clockSequenceVariantAndNodeRaw, getLeastSignificantBits())
        }
    }

    @Test
    @OptIn(UUIDExperimentalAPI::class)
    fun testIsValidString() {
        assertTrue(UUID.isValidUUIDString(SOME_UUID_STRING))
        assertTrue(UUID.isValidUUIDString("{$SOME_UUID_STRING}"))
        assertTrue(UUID.isValidUUIDString(" {$SOME_UUID_STRING}"))
        assertTrue(UUID.isValidUUIDString(" {$SOME_UUID_STRING} "))

        assertFalse(UUID.isValidUUIDString(SOME_UUID_STRING.drop(1)))
        assertFalse(UUID.isValidUUIDString(SOME_UUID_STRING.dropLast(1)))
        assertFalse(UUID.isValidUUIDString(SOME_UUID_STRING.replace('b', 'X')))
        assertNull(SOME_UUID_STRING.drop(1).toUUIDOrNull())
    }

    @Test
    fun testRandomCreation() {
        assertEquals(UUID.Version.RANDOM_BASED, UUID().version)
        assertEquals(UUID.Version.RANDOM_BASED, SecureRandom.nextUUID().version)
    }
}
