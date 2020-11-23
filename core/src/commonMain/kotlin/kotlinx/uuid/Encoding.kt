/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

internal const val UUID_BYTE_ARRAY_SIZE: Int = 16

/**
 * Encode [UUID] as a byte array in it's classic format, just like in text format, but as bytes.
 */
public fun UUID.encodeToByteArray(): ByteArray {
    val result = ByteArray(UUID_BYTE_ARRAY_SIZE)
    timeStampAndVersionRaw.copyInto(result, 0)
    clockSequenceVariantAndNodeRaw.copyInto(result, 8)
    return result
}

/**
 * Encodes [UUID] as a pair of long: there first long contains timestamp and version bits,
 * the second one consists of clock sequence, variant and node.
 */
public fun UUID.encodeToLongArray(): LongArray {
    return longArrayOf(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
}

/**
 * Creates [UUID] from bytes representation.
 */
public fun UUID(bytes: ByteArray): UUID {
    require(bytes.size == UUID_BYTE_ARRAY_SIZE) {
        "Input ByteArray should have size $UUID_BYTE_ARRAY_SIZE, but got array of ${bytes.size} bytes."
    }

    return UUID.create(
        bytes.getLongAt(0),
        bytes.getLongAt(8)
    )
}

/**
 * Creates an instance of [UUID] from [longPair] where the first long value
 * is timestamp and version bits and the second one consists of clock sequence,
 * variant and node.
 */
public fun UUID(longPair: LongArray): UUID {
    require(longPair.size == 2) { "Input LongArray should have size 2, but got array of ${longPair.size} values." }
    return UUID.create(
        longPair[0],
        longPair[1]
    )
}

private fun Long.copyInto(buffer: ByteArray, destinationOffset: Int) {
    repeat(8) { index ->
        buffer[destinationOffset + index] = (this ushr (56 - index * 8)).toByte()
    }
}

private fun ByteArray.getLongAt(offset: Int): Long {
    var result = 0L

    repeat(8) { index ->
        result = result or (this[offset + index].toLong() and 0xff shl ((7 - index) * 8))
    }

    return result
}
