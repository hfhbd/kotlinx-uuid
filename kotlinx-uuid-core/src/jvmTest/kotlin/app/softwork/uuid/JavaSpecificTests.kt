/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import java.security.*
import kotlin.random.*
import kotlin.test.*
import kotlin.uuid.Uuid

class JavaSpecificTests {
    @Test
    fun secure() {
        Uuid.random(SecureRandom().asKotlinRandom()).assertRandomGenerated()
    }

    private fun Uuid.assertRandomGenerated() {
        assertTrue(isRfcVariant)
        assertEquals(4, versionNumber)
    }
}
