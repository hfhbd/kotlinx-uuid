/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlinx.uuid.tests.SOME_UUID_STRING
import kotlin.test.*

class NsUUIDConvertingTest {

    @Test
    fun toNsUUID() {
        val kotlinUUID = UUID(SOME_UUID_STRING)
        val nsUUID = kotlinUUID.toNsUUID()
        assertEquals(SOME_UUID_STRING, nsUUID.UUIDString.toLowerCase())
    }

    @Test
    fun fromNsUUID() {
        val nsUUID = platform.Foundation.NSUUID(SOME_UUID_STRING)
        val kotlinUUID = nsUUID.toKotlinUUID()
        assertEquals(SOME_UUID_STRING, kotlinUUID.toString())
    }
}
