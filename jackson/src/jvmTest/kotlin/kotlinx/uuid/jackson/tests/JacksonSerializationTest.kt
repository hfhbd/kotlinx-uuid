/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.jackson.tests

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.*
import kotlinx.uuid.*
import kotlinx.uuid.jackson.*
import kotlin.test.*

class JacksonSerializationTest {
    private val jackson = ObjectMapper()
    private val uuidString = "1b3e4567-e99b-13d3-a476-446657420000"

    init {
        jackson.uuid()
        jackson.registerKotlinModule()
    }

    @Test
    fun testReturnThis() {
        val mapper = ObjectMapper()
        assertSame(mapper, mapper.uuid())
    }

    @Test
    fun testSerialize() {
        val serialized = jackson.writeValueAsString(UUID(uuidString))
        assertEquals("\"$uuidString\"", serialized)
    }

    @Test
    fun testDeserialize() {
        val text = "\"$uuidString\""
        assertEquals(UUID(uuidString), jackson.readValue(text))
    }

    @Test
    fun testSerializeClass() {
        val serialized = jackson.writeValueAsString(C(UUID(uuidString)))
        assertEquals("{\"uuid\":\"$uuidString\"}", serialized)
    }

    @Test
    fun testDeserializeClass() {
        val text = "{\"uuid\": \"$uuidString\"}"
        val deserialized = jackson.readValue<C>(text)
        assertEquals(C(UUID(uuidString)), deserialized)
    }

    data class C(var uuid: UUID)
}
