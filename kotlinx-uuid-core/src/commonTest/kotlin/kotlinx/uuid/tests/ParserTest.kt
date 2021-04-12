/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.tests

import kotlinx.uuid.*
import kotlin.test.*

internal const val SOME_UUID_STRING: String = "1b3e4567-e99b-13d3-a476-446657420000"

class ParserTest {
    @Test
    fun smoke() {
        parseUUID(SOME_UUID_STRING).assertFields()
        parseUUID(SOME_UUID_STRING.replace('b', 'B')).assertFields()
        parseUUID(SOME_UUID_STRING.uppercase()).assertFields()
        parseUUID(SOME_UUID_STRING.uppercase().replace('B', 'b')).assertFields()
    }

    @Test
    fun testUnexpectedCharacter() {
        assertFailsWith<UUIDFormatException> {
            parseUUID(SOME_UUID_STRING.replace('b', 'X'))
        }.let {
            assertTrue(it.message.contains("Unexpected octet character"))
            assertTrue(it.message.contains("X"))
        }

        assertFailsWith<UUIDFormatException> {
            parseUUID(SOME_UUID_STRING.replace('b', '.'))
        }.let {
            assertTrue(it.message.contains("Unexpected octet character"))
            assertTrue(it.message.contains("."))
        }

        assertFailsWith<UUIDFormatException> {
            parseUUID(SOME_UUID_STRING.replace('b', 'x'))
        }.let {
            assertTrue(it.message.contains("Unexpected octet character"))
            assertTrue(it.message.contains("x"))
        }

        assertFailsWith<UUIDFormatException> {
            parseUUID(SOME_UUID_STRING.uppercase().replace('B', 'X'))
        }.let {
            assertTrue(it.message.contains("Unexpected octet character"))
            assertTrue(it.message.contains("X"))
        }

        assertFailsWith<UUIDFormatException> {
            parseUUID(SOME_UUID_STRING.uppercase().replace('B', 'x'))
        }.let {
            assertTrue(it.message.contains("Unexpected octet character"))
            assertTrue(it.message.contains("x"))
        }

        assertFailsWith<UUIDFormatException> {
            parseUUID(SOME_UUID_STRING.uppercase().replace('B', '.'))
        }.let {
            assertTrue(it.message.contains("Unexpected octet character"))
            assertTrue(it.message.contains("."))
        }
    }

    @Test
    fun curlyBrackets() {
        parseUUID("{$SOME_UUID_STRING}").assertFields()
    }

    @Test
    fun spaces() {
        for (space in listOf(" ", "\t")) {
            parseUUID("$SOME_UUID_STRING$space").assertFields()
            parseUUID("$space$SOME_UUID_STRING$space").assertFields()
            parseUUID("$space$SOME_UUID_STRING").assertFields()
            parseUUID(SOME_UUID_STRING.replace("-", "$space-$space")).assertFields()
            parseUUID("$space{$SOME_UUID_STRING}$space").assertFields()
            parseUUID("$space{$space$SOME_UUID_STRING$space}$space").assertFields()
        }
    }

    @Test
    fun parseEmpty() {
        assertFailsWith<UUIDFormatException> {
            parseUUID("")
        }.let { cause ->
            assertEquals("UUID string is too short", cause.message.substringAfter(":").trim())
        }
    }

    @Test
    fun parseIncomplete() {
        for (size in 1 until SOME_UUID_STRING.length) {
            assertFailsWith<UUIDFormatException> {
                parseUUID(SOME_UUID_STRING.substring(0, size))
            }.let { cause ->
                assertEquals("UUID string is too short", cause.message.substringAfter(":").trim())
            }
        }
    }

    @Test
    fun parseIllegalCharacter() {
        for (replaceIndex in SOME_UUID_STRING.indices) {
            assertFailsWith<UUIDFormatException> {
                parseUUID(SOME_UUID_STRING.replaceAt(replaceIndex))
            }.let { cause ->
                assertEquals("Unexpected octet character #", cause.message.substringAfter(":").trim())
            }
        }
    }

    @Test
    fun trailing() {
        assertFailsWith<UUIDFormatException> {
            parseUUID("$SOME_UUID_STRING 000")
        }.let { cause ->
            assertEquals("extra trailing characters 000", cause.message.substringAfter(":").trim())
        }
    }

    @Test
    @Suppress("DEPRECATION")
    fun migrationFromString() {
        assertEquals(UUID(SOME_UUID_STRING), UUID.fromString(SOME_UUID_STRING))
    }

    private fun String.replaceAt(atIndex: Int): String = replaceAt(atIndex, '#')

    private fun String.replaceAt(atIndex: Int, newCharacter: Char): String = buildString(length) {
        if (atIndex > 0) {
            append(this@replaceAt, 0, atIndex)
        }
        append(newCharacter)
        if (atIndex < this@replaceAt.lastIndex) {
            append(this@replaceAt, atIndex + 1, this@replaceAt.length)
        }
    }

    private fun UUID.assertFields() {
        assertEquals(1, versionNumber)
        assertEquals(UUID.Version.TIME_BASED, version)
        assertEquals(5, variant)
        assertEquals("3d3e99b1b3e4567", timeStamp.toString(16))
        assertEquals("476", clockSequence.toString(16))
        assertEquals("446657420000", node.toString(16))

        assertMigrations()
    }

    @Suppress("DEPRECATION")
    private fun UUID.assertMigrations() {
        assertEquals(1, version())
        assertEquals(5, variant())
        assertEquals("3d3e99b1b3e4567", timestamp().toString(16))
        assertEquals("476", clockSequence().toString(16))
        assertEquals("446657420000", node().toString(16))
    }
}
