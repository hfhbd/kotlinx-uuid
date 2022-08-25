/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.test.*

class DumpHexTest {
    @Test
    fun smoke() {
        assertEquals("ff", dumpHex(0xff, 1))
        assertEquals("00ff", dumpHex(0xff, 2))
        assertEquals("0000ff", dumpHex(0xff, 3))

        assertEquals("12345678", dumpHex(0x12345678, 4))
        assertEquals("9abcdef0", dumpHex(0x9abcdef0, 4))

        assertEquals("ffffffff", dumpHex(-1, 4))
    }

    @Test
    fun testZeroes() {
        assertEquals("0f", dumpHex(0x0f, 1))
        assertEquals("f0", dumpHex(0xf0, 1))
        assertEquals("00", dumpHex(0, 1))
        assertEquals("0f00", dumpHex(0x0f00, 2))
        assertEquals("f000", dumpHex(0xf000, 2))

        assertEquals("0a", dumpHex(0x0a, 1))
        assertEquals("09", dumpHex(0x09, 1))
    }

    @Test
    fun testCut() {
        assertEquals("34", dumpHex(0x1234, 1))
    }

    private fun dumpHex(value: Long, numberOfOctets: Int): String = buildString {
        dumpHex(value, numberOfOctets, this)
    }
}
