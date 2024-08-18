/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.test.*
import kotlin.uuid.Uuid

@ExperimentalSerializationApi
class UuidSerializationTest {
    @Test
    fun smokeTest() {
        val value = Uuid.parse(SOME_UUID_STRING)
        val encoded = Json.encodeToString(UuidSerializer, value)
        assertEquals("\"$SOME_UUID_STRING\"", encoded)
        val decoded = Json.decodeFromString(UuidSerializer, encoded)

        assertEquals(value, decoded)
    }
}
