/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.gson.tests

import com.google.gson.*
import kotlinx.uuid.*
import kotlinx.uuid.gson.*
import kotlin.test.*

class GsonSerializationTest {
    private val gson: Gson = GsonBuilder().registerUUID().create()
    private val uuidString = "1b3e4567-e99b-13d3-a476-446657420000"

    @Test
    fun testSerialize() {
        val serialized = gson.toJson(UUID(uuidString))
        assertEquals("\"$uuidString\"", serialized)
    }

    @Test
    fun testDeserialize() {
        val text = "\"$uuidString\""
        assertEquals(UUID(uuidString), gson.fromJson(text, UUID::class.java))
    }

    @Test
    fun testSerializeClass() {
        val serialized = gson.toJson(C(UUID(uuidString)))
        assertEquals("{\"uuid\":\"$uuidString\"}", serialized)
    }

    @Test
    fun testDeserializeClass() {
        val text = "{\"uuid\": \"$uuidString\"}"
        val deserialized = gson.fromJson(text, C::class.java)
        assertEquals(C(UUID(uuidString)), deserialized)
    }

    data class C(var uuid: UUID)
}
