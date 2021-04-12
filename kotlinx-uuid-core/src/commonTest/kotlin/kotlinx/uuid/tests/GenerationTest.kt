/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.tests

import kotlinx.uuid.*
import kotlin.random.*
import kotlin.test.*

class GenerationTest {
    @Test
    fun smokeTest() {
        UUID.generateUUID().assertRandomGenerated()
    }

    @Test
    fun customRandomImpl() {
        UUID.generateUUID(Random(777)).assertRandomGenerated()
        UUID.generateUUID(Random(778)).assertRandomGenerated()
        assertEquals(UUID.generateUUID(Random(777)), UUID.generateUUID(Random(777)))
        assertNotEquals(UUID.generateUUID(Random(777)), UUID.generateUUID(Random(778)))
    }

    @Test
    fun testRandomExtension() {
        Random.nextUUID().assertRandomGenerated()
    }

    @Test
    fun testGenerateFromName() {
        val baseUUID = UUID(SOME_UUID_STRING)
        val generated = UUID.generateUUID(baseUUID, "test")
        assertEquals(5, generated.versionNumber)
        assertEquals(UUID.Version.NAME_BASED_SHA1, generated.version)
        assertEquals("9dc3df60-4ed1-5ea9-9e66-5c2030d5827b", generated.toString())
    }

    @Test
    fun testGenerateFromBytes() {
        val generated = UUID.generateUUID(SOME_UUID_STRING.explodeToBytes().toByteArray())
        assertEquals(5, generated.versionNumber)
        assertEquals(UUID.Version.NAME_BASED_SHA1, generated.version)
        assertEquals("29e5befd-ca93-58bf-9ef0-30f7da112935", generated.toString())
    }

    @Test
    fun testGenerateFromBytesMigration() {
        @Suppress("DEPRECATION_ERROR")
        val generated = UUID.nameUUIDFromBytes(SOME_UUID_STRING.explodeToBytes().toByteArray())
        assertEquals(5, generated.versionNumber)
        assertEquals(UUID.Version.NAME_BASED_SHA1, generated.version)
    }

    private fun UUID.assertRandomGenerated() {
        assertTrue(isRfcVariant)
        assertEquals(4, versionNumber)
    }
}
