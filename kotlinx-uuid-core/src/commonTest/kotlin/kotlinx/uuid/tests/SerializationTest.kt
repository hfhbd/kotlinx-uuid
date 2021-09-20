/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.tests

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.*
import kotlinx.uuid.*
import kotlinx.uuid.Serializer
import kotlin.test.*

@ExperimentalSerializationApi
class SerializationTest {
    @Test
    fun smokeTest() {
        assertEquals("\"$SOME_UUID_STRING\"", Json.encodeToString(UUID(SOME_UUID_STRING)))
        assertEquals(UUID(SOME_UUID_STRING), Json.decodeFromString("\"$SOME_UUID_STRING\""))
        assertTrue(UUID.serializer() is Serializer)
    }

    @Test
    fun serializeClass() {
        assertEquals(
            "{\"p\":\"$SOME_UUID_STRING\"}",
            Json.encodeToString(E(UUID(SOME_UUID_STRING)))
        )

        assertEquals(
            E(UUID(SOME_UUID_STRING)),
            Json.decodeFromString("{ \"p\": \"$SOME_UUID_STRING\" }")
        )
    }

    @Test
    fun testCustomizeSerializer() {
        assertEquals(
            "\"{$SOME_UUID_STRING}\"",
            Json.encodeToString(Serializer.WrappedCurlyBrackets, UUID(SOME_UUID_STRING))
        )
        assertEquals(
            "\"$SOME_UUID_STRING\"",
            Json.encodeToString(Serializer.Default, UUID(SOME_UUID_STRING))
        )
        assertEquals(
            "\"$SOME_UUID_STRING\"",
            Json.encodeToString(Serializer.Default, UUID(SOME_UUID_STRING))
        )
    }

    @Test
    fun testDescriptor() {
        val descriptor = Serializer.Default.descriptor

        assertEquals(PrimitiveKind.STRING, descriptor.kind)
        assertEquals(false, descriptor.isNullable)
    }

    @Serializable
    class E(val p: UUID) {
        override fun equals(other: Any?): Boolean = other is E && p == other.p
        override fun hashCode(): Int = p.hashCode()
    }
}
