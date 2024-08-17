/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid.internal

/**
 * This is a simple Kotlin SHA-1 implementation based on wiki's description.
 * This is not optimized for speed and has never been verified except for simple tests.
 */
internal class SHA1 {
    private val buffer = ByteArray(64)
    private var bufferSize = 0
    private var totalCount = 0L

    private val intView = IntArrayView(buffer)
    private val buffer80 = IntArray(80)

    private var hA: Int = 0
    private var hB: Int = 0
    private var hC: Int = 0
    private var hD: Int = 0
    private var hE: Int = 0

    init {
        reset()
    }

    internal fun reset() {
        buffer.fill(0)
        buffer80.fill(0)
        bufferSize = 0
        totalCount = 0

        hA = 0X67452301
        hB = 0xefcdab89L.toInt()
        hC = 0x98badcfeL.toInt()
        hD = 0X10325476L.toInt()
        hE = 0xc3d2e1f0L.toInt()
    }

    internal fun final(): ByteArray {
        buffer[bufferSize++] = 0x80.toByte()

        if (bufferSize >= 57) {
            buffer.fill(0, bufferSize, buffer.size)
            handleBuffer() // sets bufferSize = 0
        }

        if (bufferSize < 57) {
            buffer.fill(0, bufferSize, buffer.size)
        }

        val messageSize = totalCount shl 3
        intView[intView.size - 2] = (messageSize ushr 32).toInt()
        intView[intView.size - 1] = (messageSize and 0xffffffffL).toInt()

        handleBuffer()

        val hash = ByteArray(20)
        val view = IntArrayView(hash)
        view[0] = hA
        view[1] = hB
        view[2] = hC
        view[3] = hD
        view[4] = hE

        reset()

        return hash
    }

    internal fun update(data: ByteArray, offset: Int = 0, length: Int = data.size - offset) {
        require(offset >= 0)
        require(offset <= data.size)
        require(length >= 0)
        require(offset + length <= data.size)

        if (length == 0) return

        var currentOffset = offset
        var remaining = length
        do {
            val processed = handleBlock(data, currentOffset, remaining)
            currentOffset += processed
            remaining -= processed
        } while (remaining > 0)

        totalCount += length
    }

    private fun handleBlock(data: ByteArray, offset: Int, length: Int): Int {
        val tailSize = minOf(buffer.size - bufferSize, length)
        data.copyInto(buffer, bufferSize, offset, offset + tailSize)
        bufferSize += tailSize

        if (bufferSize == buffer.size) {
            handleBuffer()
        }

        return tailSize
    }

    private fun handleBuffer() {
        for (index in 0..15) {
            buffer80[index] = intView[index]
        }

        for (index in 16..79) {
            buffer80[index] = (
                buffer80[index - 3] xor buffer80[index - 8] xor
                    buffer80[index - 14] xor buffer80[index - 16]
                ).rollBits1()
        }

        var a = hA
        var b = hB
        var c = hC
        var d = hD
        var e = hE

        for (index in 0..19) {
            val temp = a.rollBits5() + f1(b, c, d) + e + buffer80[index] + 0x5a827999

            e = d
            d = c
            c = b.rollBitsLeft(30)
            b = a
            a = temp
        }

        for (index in 20..39) {
            val temp = a.rollBits5() + f2(b, c, d) + e + buffer80[index] + 0x6ed9eba1

            e = d
            d = c
            c = b.rollBitsLeft(30)
            b = a
            a = temp
        }

        for (index in 40..59) {
            val temp = a.rollBits5() + f3(b, c, d) + e + buffer80[index] + 0x8f1bbcdcL.toInt()

            e = d
            d = c
            c = b.rollBitsLeft(30)
            b = a
            a = temp
        }

        for (index in 60..79) {
            val temp = a.rollBits5() + f4(b, c, d) + e + buffer80[index] + 0xca62c1d6L.toInt()

            e = d
            d = c
            c = b.rollBitsLeft(30)
            b = a
            a = temp
        }

        hA += a
        hB += b
        hC += c
        hD += d
        hE += e

        bufferSize = 0
        buffer80.fill(0)
        buffer.fill(0)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun f1(m: Int, l: Int, k: Int): Int {
        return (m and l) or (m.inv() and k)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun f2(m: Int, l: Int, k: Int): Int {
        return m xor l xor k
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun f3(m: Int, l: Int, k: Int): Int {
        return (m and l) or (m and k) or (l and k)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun f4(m: Int, l: Int, k: Int): Int {
        return f2(m, l, k)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Int.rollBits1(): Int {
        return rollBitsLeft(1)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Int.rollBits5(): Int {
        return rollBitsLeft(5)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Int.rollBitsLeft(n: Int): Int {
        return (this shl n) or (this ushr (32 - n))
    }

    private class IntArrayView(private val bytes: ByteArray) {
        inline val size: Int get() = bytes.size / 4

        @Suppress("NOTHING_TO_INLINE")
        inline operator fun get(index: Int): Int {
            val startIndex = index shl 2
            return ((bytes[startIndex].toInt() and 0xff) shl 24) or
                ((bytes[startIndex + 1].toInt() and 0xff) shl 16) or
                ((bytes[startIndex + 2].toInt() and 0xff) shl 8) or
                (bytes[startIndex + 3].toInt() and 0xff)
        }

        @Suppress("NOTHING_TO_INLINE")
        inline operator fun set(index: Int, value: Int) {
            val startIndex = index shl 2

            bytes[startIndex] = (value ushr 24).toByte()
            bytes[startIndex + 1] = ((value ushr 16) and 0xff).toByte()
            bytes[startIndex + 2] = ((value ushr 8) and 0xff).toByte()
            bytes[startIndex + 3] = (value and 0xff).toByte()
        }
    }
}
