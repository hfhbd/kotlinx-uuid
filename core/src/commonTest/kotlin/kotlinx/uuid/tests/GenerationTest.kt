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

    private fun UUID.assertRandomGenerated() {
        assertTrue(isRfcVariant)
        assertEquals(4, versionNumber)
    }
}
