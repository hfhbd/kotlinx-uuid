/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.random.*
import kotlin.test.*
import kotlin.uuid.Uuid

class GenerationTest {
    @Test
    fun smokeTest() {
        Uuid.random().assertRandomGenerated()
    }

    @Test
    fun customRandomImpl() {
        Uuid.random(Random(777)).assertRandomGenerated()
        Uuid.random(Random(778)).assertRandomGenerated()
        assertEquals(Uuid.random(Random(777)), Uuid.random(Random(777)))
        assertNotEquals(Uuid.random(Random(777)), Uuid.random(Random(778)))
    }

    @Test
    fun testRandomExtension() {
        Random.nextUuid().assertRandomGenerated()
    }

    @Test
    fun testGenerateFromName() {
        val baseUUID = Uuid.parse(SOME_UUID_STRING)
        val generated = Uuid.generateUUID(baseUUID, "test")
        assertEquals(5, generated.versionNumber)
        assertEquals("9dc3df60-4ed1-5ea9-9e66-5c2030d5827b", generated.toString())
    }

    @Test
    fun testGenerateFromBytes() {
        val generated = Uuid.generateUUID(SOME_UUID_STRING.explodeToBytes().toByteArray())
        assertEquals(5, generated.versionNumber)
        assertEquals("29e5befd-ca93-58bf-9ef0-30f7da112935", generated.toString())
    }

    private fun Uuid.assertRandomGenerated() {
        assertTrue(isRfcVariant)
        assertEquals(4, versionNumber)
    }

    private fun String.explodeToBytes(): List<Byte> {
        return replace("-", "")
            .windowed(2, 2) {
                it.toString().toInt(radix = 16).toByte()
            }
    }
}
