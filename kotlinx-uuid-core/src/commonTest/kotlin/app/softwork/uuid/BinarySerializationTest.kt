/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import kotlinx.serialization.*
import kotlinx.serialization.cbor.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import kotlin.test.*
import kotlin.uuid.Uuid

@ExperimentalSerializationApi
class BinarySerializationTest {
    @Test
    fun smokeTest() {
        val value = Uuid.parse(SOME_UUID_STRING)
        val encoded = Cbor.encodeToHexString(BinarySerializer, value)
        val decoded = Cbor.decodeFromHexString(BinarySerializer, encoded)

        assertEquals(value, decoded)
    }

    @Test
    fun testDeadDecoder() {
        val decoder = object : AbstractDecoder() {
            override val serializersModule: SerializersModule
                get() = EmptySerializersModule()

            override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
                return 777
            }
        }

        assertFailsWith<SerializationException> {
            BinarySerializer.deserialize(decoder)
        }
    }

    @Test
    fun testWithJson() {
        val initial = Uuid.parse(SOME_UUID_STRING)
        val encoded = Json.encodeToString(BinarySerializer, initial)
        val decoded = Json.decodeFromString(BinarySerializer, encoded)
        assertEquals(initial, decoded)
    }

    @Test
    fun testWrongNumberOfElementsInArray() {
        assertFailsWith<SerializationException> {
            Json.decodeFromString(BinarySerializer, "[1, 2, 3]")
        }
    }

    @Test
    fun testDescriptor() {
        assertEquals(StructureKind.LIST, BinarySerializer.descriptor.kind)
    }
}
