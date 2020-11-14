/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.tests

import kotlinx.uuid.*
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaConvertingTest {

    @Test
    fun toJavaUUID() {
        val kotlinUUID = UUID(SOME_UUID_STRING)
        val javaUUID = kotlinUUID.toJavaUUID()
        assertEquals(SOME_UUID_STRING, javaUUID.toString())
    }

    @Test
    fun fromJavaUUID() {
        val javaUUID = java.util.UUID.fromString(SOME_UUID_STRING)
        val kotlinUUID = javaUUID.toKotlinUUID()
        assertEquals(SOME_UUID_STRING, kotlinUUID.toString())
    }
}
