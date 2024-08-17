/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.test.*
import kotlin.uuid.Uuid

class NsUUIDConvertingTest {

    @Test
    fun toNsUUID() {
        val kotlinUUID = Uuid.parse(SOME_UUID_STRING)
        val nsUUID = kotlinUUID.toNsUUID()
        assertEquals(SOME_UUID_STRING, nsUUID.UUIDString.lowercase())
    }

    @Test
    fun fromNsUUID() {
        val nsUUID = platform.Foundation.NSUUID(SOME_UUID_STRING)
        val kotlinUUID = nsUUID.toKotlinUuid()
        assertEquals(SOME_UUID_STRING, kotlinUUID.toString())
    }
}
