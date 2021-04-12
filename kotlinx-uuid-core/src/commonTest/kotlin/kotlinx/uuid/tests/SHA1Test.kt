/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.tests

import kotlinx.uuid.internal.*
import kotlin.test.*

class SHA1Test {
    private val sha1 = SHA1()

    @Test
    fun smokeTest() {
        sha1.update(ByteArray(1))
        val result = sha1.final()

        assertEquals(
            listOf<Byte>(
                91, -87, 60, -99, -80, -49, -7, 63,
                82, -75, 33, -41, 66, 14, 67, -10, -19, -94,
                120, 79
            ),
            result.asList()
        )
    }

    @Test
    fun testDifferentSizesNoCrash() {
        repeat(8192) { size ->
            sha1.update(ByteArray(size))
            assertNotNull(sha1.final())
        }
    }

    @Test
    fun testDifferentSizesAggregate() {
        repeat(8192) { size ->
            sha1.update(ByteArray(size))
        }
        assertEquals(
            "e4cbd173 34f4f949 4e237381 446e7f6f cc2a1136",
            sha1.final().toHex()
        )
    }

    @Test
    fun testEmpty() {
        assertEquals(
            "da39a3ee 5e6b4b0d 3255bfef 95601890 afd80709",
            sha1("")
        )
    }

    @Test
    fun testAbc() {
        assertEquals(
            "a9993e36 4706816a ba3e2571 7850c26c 9cd0d89d",
            sha1("abc")
        )
    }

    @Test
    fun testFox() {
        assertEquals(
            "2fd4e1c6 7a2d28fc ed849ee1 bb76e739 1b93eb12",
            sha1("The quick brown fox jumps over the lazy dog")
        )
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun testAbcdLong() {
        assertEquals(
            "84983e44 1c3bd26e baae4aa1 f95129e5 e54670f1",
            sha1("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq")
        )
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun testAbcdLong2() {
        assertEquals(
            "a49b2446 a02c645b f419f995 b6709125 3a04a259",
            sha1(
                "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijkl" +
                    "mnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"
            )
        )
    }

    @Test
    fun testMillionA() {
        val buffer = ByteArray(1000)
        buffer.fill('a'.code.toByte())

        repeat(1000) {
            sha1.update(buffer)
        }

        assertEquals(
            "34aa973c d4c4daa4 f61eeb2b dbad2731 6534016f",
            sha1.final().toHex()
        )
    }

    private fun sha1(input: String): String {
        sha1.reset()
        sha1.update(input.encodeToByteArray())
        return sha1.final().toHex()
    }

    private fun ByteArray.toHex(): String = buildString(size * 3) {
        for (index in this@toHex.indices) {
            val value = this@toHex[index].toInt() and 0xff

            if (value < 16) {
                append('0')
            }
            append(value.toString(16))

            if ((index + 1) and 3 == 0) {
                append(' ')
            }
        }

        while (this[lastIndex] == ' ') {
            deleteAt(lastIndex)
        }
    }
}
