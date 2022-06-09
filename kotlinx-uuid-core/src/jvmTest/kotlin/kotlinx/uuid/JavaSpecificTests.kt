/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlinx.uuid.*
import java.security.*
import kotlin.random.*
import kotlin.test.*

class JavaSpecificTests {
    @Test
    fun secure() {
        UUID.generateUUID(SecureRandom().asKotlinRandom()).assertRandomGenerated()
    }

    private fun UUID.assertRandomGenerated() {
        assertTrue(isRfcVariant)
        assertEquals(4, versionNumber)
    }
}
